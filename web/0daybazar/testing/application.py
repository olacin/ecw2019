from flask            import Flask, render_template
from flask            import request, Response, send_from_directory
import json

application = Flask(__name__)

@application.route('/.bzr/<path:filename>')
def bazaar(filename):
    print(application.root_path + '/.bzr/')
    return send_from_directory(application.root_path + '/.bzr/', filename, conditional=True)


@application.route("/enroll", methods=['POST'])
def enroll():
    email = request.form.get('email', '')

    with open('static/database.json', 'rw') as f:
        data = json.load(f)
        for d in data:
            if email == d:
                return "After this, there is no turning back. You take the blue pill - the story ends, you wake up in your bed and believe whatever you want to believe. You take the red pill - you stay in Wonderland, and I show you how deep the rabbit hole goes."

    return "Follow the white rabbit !"


@application.route('/static/database.json', methods=['GET'])
@application.errorhandler(401)
def endpoint():
    return Response('We are the samurai, the keyboard cowboys.', 401, {'The Plague':"There is no right and wrong. There's only fun and boring."})


@application.route("/", methods=['POST', 'GET'])
def welcome(): 
    return render_template('index.html', data="Razor: Remember, hacking is more than just a crime. It's a survival trait.")


if __name__ == '__main__':
    application.run()
