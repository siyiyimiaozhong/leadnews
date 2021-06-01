function WebSocketApi(){
  this.ws = null
}
WebSocketApi.prototype={
  init : function(clientId,excutor){
    try{
      let url = "ws://"+process.env.WS_SERVER+"/ws?clientId=";
      this.ws = new WebSocket(url+clientId)
      this.ws.onopen = excutor.open
      this.ws.onclose = excutor.close
      this.ws.onerror = excutor.error
      this.ws.onmessage = excutor.message
    }catch (e) {
      alert("Sorry 您的浏览器支持 WebSocket!")
    }
  }
}

export default  new WebSocketApi()
