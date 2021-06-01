function D3ToCore(){
  this.width = 400
  this.height = 400
  this.svg = null
  this.x = null
  this.y = null
  this.center=null
  this.previous=null
  this.points = []
}
D3ToCore.prototype={
  init : function(d3,svg,width,height,color,top,right,bottom,left){
    this.width = width
    this.height = height
    this.svg = svg
    let centerX = parseInt(this.width/2)
    let points = centerX+" "+top+","+left+" "+(height-bottom)+","+(width-right)+" "+(height-bottom);
    console.log(points)
    this.svg.append('polygon')
      .attr('points',points)
      .attr("fill",color)
      .attr("stroke",color)
      .attr("stroke-width","0.7");
  }
}

export  default  new D3ToCore()
