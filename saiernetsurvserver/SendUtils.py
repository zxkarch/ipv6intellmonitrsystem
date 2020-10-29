#-*-coding:utf-8-*-
import json
import requests
class SendUtils:
    urlserver = "http://192.168.0.101:8080/push/one"
    def sendReqtoServer(self, devn, devt, rtime,rval, imgurl):
        resp ={}
        resp['version']='2.0'
        data={}
        data['devicename']=devn
        data['typenote']=devt
        data['rtime']=rtime
        data['rvalue']=rval
        data['imgurl']=imgurl
        resp['data']=data
        # print(json.dumps(resp))
        requests.post(self.urlserver,json=resp)