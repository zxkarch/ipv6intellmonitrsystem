#encoding:utf-8

from werkzeug.utils import secure_filename
from flask import Flask, render_template, jsonify, request, make_response, send_from_directory, abort
import time
import os
from Pic_str import *
import base64
import threading
from ImgUtils import *
from FireDetect import *
from SendUtils import *



# def thread_webApp():    
app = Flask(__name__)
UPLOAD_FOLDER_R = 'Images/robotcam'
UPLOAD_FOLDER_W = 'Images/wlcamera'
app.config['UPLOAD_FOLDER_R'] = UPLOAD_FOLDER_R
app.config['UPLOAD_FOLDER_W']=UPLOAD_FOLDER_W
basedir = os.path.abspath(os.path.dirname(__file__))
# ALLOWED_EXTENSIONS = set(['png', 'jpg', 'JPG', 'PNG', 'gif', 'GIF'])
ALLOWED_EXTENSIONS = set(['jpg', 'JPG'])
fireDetect = FireDetect()
sendUtils = SendUtils()

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

# @app.route('/upload')
# def upload_test():
#     return render_template('up.html')

def getDateStr():
    return (time.strftime('%Y-%m-%d',time.localtime(time.time())), time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))

# 上传文件
@app.route('/up_photo', methods=['POST'], strict_slashes=False)
def api_upload():
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER_R'])
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    (shortDate, longDate)=getDateStr()
    file_dir = file_dir+"/"+shortDate
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    # f = request.files['photo']
    # if f and allowed_file(f.filename):
    #     fname = secure_filename(f.filename)
    #     print(fname)
    imgstr = request.json
    if imgstr:
        image_data = base64.b64decode(imgstr["image_str"])
        
        ext = "jpg"
        new_filename = Pic_str().create_uuid() + '.' + ext
        # f.save(os.path.join(file_dir, new_filename))
        fpath = os.path.join(file_dir, new_filename)
        # print(fpath)
        file = open(fpath, 'wb')
        file.write(image_data)
        file.close()
        isfire = fireDetect.detect_image(fpath)
        sendUtils.sendReqtoServer("园区厂区工作间","巡航车载相机",longDate,isfire,shortDate+"/"+new_filename)
        return jsonify({"success": 0, "msg": "上传成功"})
    else:
        return jsonify({"error": 1001, "msg": "上传失败"})

@app.route('/download/<string:optcam>/<string:datetimestr>/<string:filename>', methods=['GET'])
def download(optcam, datetimestr, filename):
    if (optcam == "robotcam" or optcam == "wlcamera"):
        if (optcam == "robotcam"):
            file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER_R'])
        else:
            file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER_W'])
        if request.method == "GET":
            file_dir = file_dir+"/"+datetimestr
            if os.path.isfile(os.path.join(file_dir, '%s' % filename)):
                return send_from_directory(file_dir, filename, as_attachment=True)
            pass


# show photo
@app.route('/show/<string:optcam>/<string:datetimestr>/<string:filename>', methods=['GET'])
def show_photo(optcam, datetimestr, filename):
    if (optcam == "robotcam" or optcam == "wlcamera"):
        if (optcam == "robotcam"):
            file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER_R'])
        else:
            file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER_W'])
        if request.method == 'GET':
            if filename is None:
                pass
            else:
                print (os.path.join(file_dir+"/"+datetimestr, '%s' % filename))
                image_data = open(os.path.join(file_dir+"/"+datetimestr, '%s' % filename), "rb").read()
                response = make_response(image_data)
                response.headers['Content-Type'] = 'image/jpeg'
                return response
        pass
    pass
    # app.run()

# t_webApp = threading.Thread(name='Web App', target=thread_webApp)
# t_webApp.setDaemon(True)
# t_webApp.start()

resbasedir = "./Images/wlcamera"
threademo=threading.Thread(target=ImgUtils().getcameraimgbyusb,args=(resbasedir,))
threademo.start()


if __name__ == '__main__':
#     resbasedir = os.path.join(basedir, app.config['UPLOAD_FOLDER_W'])
#     threademo=threading.Thread(target=ImgUtils().getcameraimgbyusb,args=(resbasedir,))
#     threademo.setDaemon(True)
#     threademo.start()
    app.run(host="192.168.0.102",port=5000)