from flask import (
    flash,
    render_template,
    redirect,
    request,
    session,
    url_for,
    Response
)
from twilio.twiml.voice_response import VoiceResponse
from flask import Flask
import flask
import urllib2
from datetime import datetime
import requests


app = Flask(__name__)

bytesPersecond = 64000

def twiml(resp):
    resp = flask.Response(str(resp))
    resp.headers['Content-Type'] = 'text/xml'
    return resp

@app.route('/')
@app.route('/ivr')
def home():
    return render_template('index.html')


@app.route("/streamposition/<time>/<digits>/<fastforward>", methods=['GET'])
def streamposition(time,digits,fastforward):
    pudId = int(digits)
    now = datetime.now()
    later = datetime.strptime(time, "%d-%b-%Y (%H:%M:%S.%f)")
    secs = (now-later ).total_seconds()
    print "streamposition secs "+str(secs)
    
    def generate():
            req = urllib2.Request("https://flexcinestuff.s3.amazonaws.com/"+str(pudId)+".mp3")
            if(int(fastforward)==6):
                req.add_header('Range', 'bytes=' +str((int(secs)+10)*bytesPersecond)+"-")
            elif(int(fastforward)==4):
                req.add_header('Range', 'bytes=' +str((int(secs)-10)*bytesPersecond)+"-")
            fwav = urllib2.urlopen(req)
            data = fwav.read(1024)
            while data:
                yield data
                data = fwav.read(1024)
    return Response(generate(), mimetype="audio/mpeg")

@app.route("/ivr/startstream/<digits>", methods=['GET'])
def startstream(digits):
    #response = VoiceResponse()
    #response.say("Please wait",voice="alice", language="en-GB", loop=1)
    #pudId = request.form['Digits']
    pudId = int(digits)
    nLoops = 0
    #response.gather(numDigits=1, action=url_for('streamposition',time=((nLoops*1024)/bytesPersecond)), method="POST")
    print "startstream "+str(pudId)        
    def generate():
            req = urllib2.Request("https://flexcinestuff.s3.amazonaws.com/"+str(pudId)+".mp3")
            #req.add_header('Range', "bytes=0-")
            fwav = urllib2.urlopen(req)
            data = fwav.read(1024)
            while data:
                yield data
                data = fwav.read(1024)
                #nLoops = nLoops+1
    return Response(generate(), mimetype="audio/mpeg")

@app.route("/ivr/twstartstreamposition/<time>/<pudId>", methods=['POST'])
def twstartstreamposition(time,pudId):
    response = VoiceResponse()
    #response.play(url=url_for('startstream',digits=pudId), loop=1)
    now = datetime.now()
    fastforward = request.form['Digits']
    with response.gather(
        num_digits=1, action=url_for('twstartstreamposition',time=now.strftime("%d-%b-%Y (%H:%M:%S.%f)"),pudId=pudId), method="POST"
    ) as g:
        g.play(url=url_for('streamposition',time = time,digits=pudId,fastforward =fastforward), loop=1)
        
    return twiml(response)

@app.route("/ivr/twstartstream", methods=['POST'])
def twstartstream():
    response = VoiceResponse()
    digits = request.form['Digits']
    #response.play(url=url_for('startstream',digits=pudId), loop=1)
    now = datetime.now()
    with response.gather(
        num_digits=1, action=url_for('twstartstreamposition',time=now.strftime("%d-%b-%Y (%H:%M:%S.%f)"),pudId=digits), method="POST"
    ) as g:
        g.play(url=url_for('startstream',digits=digits), loop=1)
        
    return twiml(response)



def _gotopub(response):
    with response.gather(
        numDigits=8, action=url_for('twstartstream'), method="POST"
    ) as g:
        g.say("Please enter the publication 8 digit ID",
              voice="alice", language="en-GB", loop=1)

    return response

@app.route("/ivr/usersubsribe", methods=['POST'])
def usersubsribe():
    response = VoiceResponse()
    digits = request.form['Digits']
    user = request.form['From']
    r = requests.post('http://httpbin.org/post', json={"action": "Subsribe","channelId":digits,"number":user})
    response.say("Thank you for subsribing")
    response.hangup()
        
    return twiml(response)

def _gotosub(response):
    with response.gather(
        numDigits=4, action=url_for('usersubsribe'), method="POST"
    ) as g:
        g.say("Please enter the channels 4 digit ID to subsribe",
              voice="alice", language="en-GB", loop=1)

    return response

@app.route('/ivr/welcome', methods=['POST'])
def welcome():
    response = VoiceResponse()
    user = request.form['From']
    with response.gather(
        num_digits=1, action=url_for('menu'), method="POST"
    ) as g:
        g.say(message="Welcome to Zuba. "+
              "Please press 1 to go to audio publication. " +
              "Press 2 to subsribe to channel. ", loop=1)
    return twiml(response)


@app.route('/ivr/menu', methods=['POST'])
def menu():
    selected_option = request.form['Digits']
    option_actions = {'1': _gotopub,
                      '2': _gotosub}

    if option_actions.has_key(selected_option):
        response = VoiceResponse()
        option_actions[selected_option](response)
        return twiml(response)

    return _redirect_welcome()


@app.route('/ivr/planets', methods=['POST'])
def planets():
    selected_option = request.form['Digits']
    option_actions = {'2': "+12024173378",
                      '3': "+12027336386",
                      "4": "+12027336637"}

    if selected_option in option_actions:
        response = VoiceResponse()
        response.dial(option_actions[selected_option])
        return twiml(response)

    return _redirect_welcome()


# private methods


def _redirect_welcome():
    response = VoiceResponse()
    response.say("Returning to the main menu", voice="alice", language="en-GB")
    response.redirect(url_for('welcome'))

    return twiml(response)
