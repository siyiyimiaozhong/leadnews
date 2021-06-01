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
  init : function(d3,svg,width,height){
    this.width = width
    this.height = height
    this.svg = svg
    console.log(svg)
    this.x = d3.scaleLinear().domain([-2, 2]).range([100, width-100]);
    this.y = d3.scaleLinear().domain([-2, 2]).range([height-100, 100]);
    this.previous = this.center={x:width/2,y:height/2}
  },
  position :function (r,index) {
    for (let i = 0; i <r+30; i+=30) {
        let y=i
        if(y>r)y=r
        let r2 = Math.pow(r,2),y2=Math.pow(y,2);
        let x = Math.sqrt(r2-y2,2)
      if(y==r){
        y=y-3
      }
        if(x>=0){
          this.stepCircle(index,this.quadrant(x,y,1,-1))
          this.stepCircle(index,this.quadrant(x,y,-1,-1))
          this.stepCircle(index,this.quadrant(x,y,-1,1))
          this.stepCircle(index,this.quadrant(x,y,1,1))
        }
    }
  },
  quadrant : function(x,y,stepX,stepY){
    return {x:this.center.x+x*stepX,y:this.center.y+y*stepY}
  },
  stepCircle : function(index,pos){
    this.points[index]=this.points[index]?this.points[index]:[]
    this.points[index].push(pos)
    this.svg.append('circle').attr('r',3).attr('class','point').attr("fill", "#f7503e").attr('cx',pos.x).attr('cy',pos.y);

  },
  drawLine:function(a,b){
    this.svg.append('line')
      .attr('x1',a.x)
      .attr("y1",a.y)
      .attr('x2',b.x)
      .attr("y2",b.y)
      .attr("stroke","#f7503e")
      .attr("stroke-width","0.7");
  },
  drawLineFor:function(){
    for(let i=0;i<this.points[0].length;i++) {
      this.drawLine(this.points[0][i], this.points[1][i])
      this.drawLine(this.points[2][i], this.points[1][i])
      this.drawLine(this.points[0][i], this.points[2][i])
      if (i > 3) {
        this.drawLine(this.points[2][i], this.points[3][i-4])
        this.drawLine(this.points[3][i], this.points[4][i-4])
        // this.drawLine(this.points[2][i], this.points[2][i-4])
        // this.drawLine(this.points[1][i], this.points[2][i - 4])
        // this.drawLine(this.points[1][i], this.points[0][i - 4])
        // this.drawLine(this.points[1][i], this.points[1][i - 4])
      }
    }
  },
  draw:function(){
    this.position(200,0)
    this.position(230,1)
    this.position(260,2)
    this.position(290,3)
    this.position(400,4)
    this.drawLineFor()
  }
}

export  default  new D3ToCore()
