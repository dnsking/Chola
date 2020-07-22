import json
import boto3
from botocore.errorfactory import ClientError
import string
import datetime
import pymysql
import pymysql.cursors

bucketName = 'flexcinestuff'


try:
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,   connect_timeout=5)
    #createPropertyTable()
    #createMessageTable()
    #createTable()
    #createChannelTable()
    #createSubChannelTable()
except pymysql.MySQLError as e:
    print("ERROR: Unexpected error: Could not connect to MySQL instance.")
    print(e)
   
def createTable():
    with conn.cursor() as cur:
        cur.execute("create table IF NOT EXISTS ContentTable( channelId varchar(255) NOT NULL, title varchar(255) NOT NULL, pubId varchar(255) NOT NULL, pubTime varchar(255) NOT NULL, PRIMARY KEY (pubId))")
        conn.commit()
    print("PropertySeenDBTable created")
    
def createChannelTable():
    with conn.cursor() as cur:
        cur.execute("create table IF NOT EXISTS ChannelsTable( userName varchar(255) NOT NULL,channelId varchar(255) NOT NULL, about varchar(255) NOT NULL, password varchar(255) NOT NULL, dateCreated varchar(255) NOT NULL,  PRIMARY KEY (channelId))")
        conn.commit()
    print("PropertySeenDBTable created")
    
def createSubChannelTable():
    with conn.cursor() as cur:
        cur.execute("create table IF NOT EXISTS ChannelSubsContentTable(itemid int NOT NULL AUTO_INCREMENT, number varchar(255) NOT NULL, channelId varchar(255) NOT NULL ,  PRIMARY KEY (itemid))")
        conn.commit()
    print("PropertySeenDBTable created")

def lambda_handler(event, context):
    try:
        conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,   connect_timeout=5)
        #createPropertyTable()
        #createMessageTable()
        #createTable()
        #createChannelTable()
        #createSubChannelTable()
        createSubChannelTable()
    except pymysql.MySQLError as e:
        print("ERROR: Unexpected error: Could not connect to MySQL instance.")
        print(e)
    # TODO implement
    if event['action'] == 'PutUrl':
        return putFile(event['Key'])
    elif event['action'] == 'MakePublic':
        return makePublic(event['Key'])
    elif event['action'] == 'AddContent':
        return addContent(event)
    elif event['action'] == 'GetUrl':
        return getFile(event['Key'])
    elif event['action'] == 'CreateChannel':
        return addChannel(event)
    elif event['action'] == 'ChannelLogin':
        return channelLogin(event)
    elif event['action'] == 'Invite':
        return inviteusers(event['users'], event['channelId'], event['userName'])
    elif event['action'] == 'Subsribe':
        return subscribe(event['channelId'], event['number'])
    elif event['action'] == 'AddUser':
        return addUser(event['phoneNumber'],event['userName'],event['contributionId'] ,event['type'],event['lat'],event['lon'],event['timeExpiration'] ,event['description'],event['plates'],event['color'],event['match'])
    elif event['action'] == 'GetContent':
        return getContentFor(event['channelId'])
    elif event['action'] == 'SendMessage':
        return sendMessage(event['to'],event['from'],event['message'],event['time'],event['type'])
    elif event['action'] == 'CheckMessages':
        return getMessages(event['to'])
    
    elif event['action'] == "NotifyNewId":
        return notifyNewId(event['phoneNumbers'],event['phoneNumber'])
    elif event['action'] == "AddDeviceId":
        return addDeviceId(event['phoneNumber'],event['deviceId'])
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }

def getChannelResultsFromRow(rows):
    #conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,  connect_timeout=5)
    data = []
    for row in rows:
        item = { "number":row["number"], "channelId":row["channelId"]}
        data.append(item)
    return data
    
def getSubsFor(channelId):
    data = []
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,charset='utf8mb4',cursorclass=pymysql.cursors.DictCursor,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('SELECT * FROM ChannelSubsContentTable WHERE channelId=%s',(channelId))
        rows = cur.fetchall()
        data = getChannelResultsFromRow(rows)
    return data
    
def subscribe(channelId,number):
    #channelId = getContentForVideo(videoid)[0]['channelId']
    conn = pymysql.connect(rds_host, user=name, charset='utf8',init_command='SET NAMES UTF8', passwd=password,db=db_name,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('insert into ChannelSubsContentTable(number, channelId) Values(%s, %s) ',(number,channelId))
        conn.commit()
    return "added"
    
def inviteusers(users,channelId,channelname):
    sns = boto3.client('sns')
    for user in users:
        sns.publish(PhoneNumber = user, Message=channelname+ ' invites you to join their Chonal channel. Please dial +12567332479 and join with the Channel ID '+str(channelId) )
    return "sent"
def makePublic(video_id):
    boto3.resource('s3').ObjectAcl(bucketName,video_id).put(ACL='public-read')
    return "public success: "+video_id
    
def notifyUsers(channelId,channelname,videoid,title):
    users = getSubsFor(channelId)
    sns = boto3.client('sns')
    for user in users:
        print("user "+str(user["number"]))
        sns.publish(PhoneNumber = user["number"], Message=channelname+ ' has posted a new podcast titled '+title+'. Please dial +12567332479 and listed with the Audio Publication ID '+str(videoid) )
    return "sent"
    
def getUsersResultsFromRow(rows):
    #conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,  connect_timeout=5)
    data = []
    for row in rows:
        item = { "userName":row["userName"], "password":row["password"]}
        data.append(item)
    return data
    
def channelLogin(dataN):
    data = []
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,charset='utf8mb4',cursorclass=pymysql.cursors.DictCursor,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('SELECT * FROM ChannelsTable WHERE userName=%s AND password=%s',(dataN['userName'],dataN['password']))
        rows = cur.fetchall()
        data = getUsersResultsFromRow(rows)
    return str((len(data)>0))
    
def addChannel(data):
    conn = pymysql.connect(rds_host, user=name, charset='utf8',init_command='SET NAMES UTF8', passwd=password,db=db_name,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('insert into ChannelsTable(channelId, userName, about , dateCreated, password) Values(%s, %s, %s, %s, %s) ',(data['channelId'],data['userName'],data['about'],data['dateCreated'],data['password']))
        conn.commit()
    return "added"
        
def login(data):
    conn = pymysql.connect(rds_host, user=name, charset='utf8',init_command='SET NAMES UTF8', passwd=password,db=db_name,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('replace into ChannelsTable(channelId, userName, about , dateCreated, password) Values(%s, %s, %s, %s, %s) ',(data['channelId'],data['userName'],data['about'],data['dateCreated'],data['password']))
        conn.commit()
    
def addContent(data):
    conn = pymysql.connect(rds_host, user=name, charset='utf8',init_command='SET NAMES UTF8', passwd=password,db=db_name,  connect_timeout=5)
    with conn.cursor() as cur:
        cur.execute('replace into ContentTable(channelId, title, pubId , pubTime) Values(%s, %s, %s, %s) ',(data['channelId'],data['title'],data['pubId'],data['pubTime']))
        conn.commit()
    
    notifyUsers(data['channelId'],data['userName'],data['pubId'],data['title'])
    makePublic(data['pubId']+'.mp3')
    return "done"
        
def getContentResultsFromRow(rows):
    data = []
    for row in rows:
        item = {"channelId":row["channelId"], "title":row["title"], "pubId":row["pubId"], "pubTime":row["pubTime"]}
        data.append(item)
    return data
def getContentFor(channelId):
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,init_command='SET NAMES UTF8', charset='utf8',use_unicode=True,cursorclass=pymysql.cursors.DictCursor,  connect_timeout=5)
    data = []
    with conn.cursor() as cur:
        cur.execute("SELECT * FROM ContentTable where channelId=%s",channelId)
        rows = cur.fetchall()
        data = getContentResultsFromRow(rows)
    return data
    
def getContentForVideo(pubId):
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name,init_command='SET NAMES UTF8', charset='utf8',use_unicode=True,cursorclass=pymysql.cursors.DictCursor,  connect_timeout=5)
    data = []
    with conn.cursor() as cur:
        cur.execute("SELECT * FROM ContentTable where pubId=%s",pubId)
        rows = cur.fetchall()
        data = getContentResultsFromRow(rows)
    return data
    
def deletePub(pubId):
    conn = pymysql.connect(rds_host, user=name, passwd=password,db=db_name, charset='utf8',init_command='SET NAMES UTF8',cursorclass=pymysql.cursors.DictCursor,  connect_timeout=5)
    data = []
    with conn.cursor() as cur:
        cur.execute("DELETE FROM ContentTable WHERE pubId=%s",pubId)
        conn.commit()
    return data
        
def sendMessage(to,fromP,message,time,type):
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='GivderMessages', Item={
        'toMsg': {'S': str(to)}
    ,  'timeMsg': {'S': str(time)}
    ,  'messageMsg': {'S': str(message)}
    ,  'fromMsg': {'S': str(fromP)}
    ,  'typeMsg': {'S': str(type)}})
    
    return "done"
def getMessages(to):
    items = []
    dynamodb_client = boto3.client('dynamodb')
    response = dynamodb_client.query(TableName='GivderMessages',KeyConditionExpression='toMsg = :toMsg',ExpressionAttributeValues={':toMsg': {'S': to}})
    print(response)
    for item in response['Items']:
        body = {  'to': item['toMsg']['S'],  'time': item['timeMsg']['S'],  'message': item['messageMsg']['S']
            ,  'from': item['fromMsg']['S'],  'type': item['typeMsg']['S']}
        items.append(body)
    print(items)
    return items
def getAllUnmatched():
    dynamodb_client = boto3.client('dynamodb')
    cognitoclient = boto3.client('cognito-idp')

    more_pages = True
    pagination_token = None
    all_pages = []
    users=[]
    items = []
    while more_pages:
        params = {"UserPoolId":USER_POOL_ID,"ClientId":CLIENT_ID}
        if pagination_token is not None:
            params["PaginationToken"] = pagination_token
            response = cognitoclient.list_users(UserPoolId=USER_POOL_ID,PaginationToken= pagination_token)
        else:
            response = cognitoclient.list_users(UserPoolId=USER_POOL_ID)
        
        #pagination_token = response['PaginationToken']
        page = response['Users']
        for user in page:
            users.append(user['Attributes'][2]['Value'])
        more_pages = len(page) == 60


    for user in users:
        response = dynamodb_client.query(TableName='GivderUserDescription',KeyConditionExpression='PhoneNumber = :PhoneNumber',ExpressionAttributeValues={':PhoneNumber': {'S': user}})
        for item in response['Items']:
            body = {  'phoneNumber': item['PhoneNumber']['S'],  'contributionId': item['ContributionId']['S'],  'userName': item['UserName']['S']
            ,  'type': item['Type']['S'],  'lat': item['Lat']['S'],  'lon': item['Lon']['S'],  'timeExpiration': item['TimeExpiration']['S']
            ,  'description': item['Description']['S'],  'plates': item['Plates']['S'],  'color': item['Color']['S'],  'match': item['Match']['S']}
            items.append(body)
    print(users)
    print(items)
    return items
def addUser(phoneNumber, userName,contributionId, type,lat,lon,timeExpiration,description,plates,color,match):
    
    
    
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='GivderUserDescription', Item={
        'PhoneNumber': {'S': str(phoneNumber)}
    ,  'ContributionId': {'S': str(contributionId)}
    ,  'UserName': {'S': str(userName)}
    ,  'Type': {'S': str(type)}
    ,  'Lat': {'S': str(lat)}
    ,  'Lon': {'S': str(lon)}
    ,  'TimeExpiration': {'S': str(timeExpiration)}
    ,  'Description': {'S': str(description)}
    ,  'Plates': {'S': str(plates)}
    ,  'Color': {'S': str(color)}
    ,  'Match': {'S': str(match)} })
    
    return "done"

def checkUsers(users):
    s3 = boto3.client('s3')
    doUsersExist = []
    for user in users:
        try:
            s3.head_object(Bucket=bucketName, Key=user)
            doUsersExist.append(True)
        except ClientError:
            # Not found
            doUsersExist.append(False)
    return doUsersExist
def putFile(video_id):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('put_object', Params={'Bucket':bucketName, 'Key':video_id}, ExpiresIn=21600, HttpMethod='PUT')
    
def getFile(key):
    s3 = boto3.client('s3', config=boto3.session.Config(signature_version='s3v4'))
    return s3.generate_presigned_url('get_object', Params={'Bucket':bucketName, 'Key':key}, ExpiresIn=21600)
    

def addDeviceId(phoneNumber,device_id):
    
    dynamodb = boto3.client('dynamodb')
    dynamodb.put_item(TableName='TringSubs',Item={'PhoneNumber': {'S': str(phoneNumber)}, 'DeviceId': {'S': str(device_id)}})
    
    return "success"
            
    
def getSubs(phoneNumbers):
    
    dynamodb = boto3.client('dynamodb')
    data = []
    for phoneNumber in phoneNumbers:
        response = dynamodb.get_item(Key={'PhoneNumber': {'S': phoneNumber}},  TableName='TringSubs')
        print(response)
        try:
            device = response['Item']['DeviceId']['S']
            data.append(device)
        except:
            ""
    return data
