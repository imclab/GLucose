package glucose.output;
import glucose.model.Point;
import glucose.transform.FlippedRGBList;

import java.util.ArrayList;

import oscP5.*;
import netP5.*;

public class OSCOutput {
	public float min_x=256*256;
	public float max_x=-256*256;
	public float min_y=256*256;
	public float max_y=-256*256;
	public float min_z=256*256;
	public float max_z=-256*256;
	
	public Point zp;
	
	ArrayList<Point> pointList = new ArrayList<Point>();
	
	protected int[][] flippedRGBList;
	protected OscP5 oscP5;
	// NETWORK ADDRESSES
	NetAddress bone_front = new NetAddress("192.168.1.28", 9001);
	NetAddress bone_rear = new NetAddress("192.168.1.29", 9001);
	protected byte[] msgarr;
	protected OscMessage msg;
	ArrayList<Point> mappedPointListRear = new ArrayList<Point>();
	ArrayList<Point> mappedPointListFront = new ArrayList<Point>();

	//ArrayList[] channelList = new ArrayList[]();

	ArrayList[][][] pointMap = new ArrayList[128][256][128];

	ArrayList<Boolean> masterFlippedFront = new ArrayList<Boolean>();
	ArrayList<Boolean> masterFlippedRear = new ArrayList<Boolean>();
	
	public OSCOutput()
	{
		flippedRGBList=new FlippedRGBList().getFlippedRGBList();
	}
	public  void createPointMapStructure()
	{
        // there are five cubes per channel (A,B,C,D)
      // After five cubes of data on mappedpointlist, it starts sending points to the next channel

      // If you need a blank cube, then add this for each cube that needs to be blank.
      // for(int i=0; i<(16*3*4); i++) { mappedPointListFront.add(zp);}

	   for (int i=0; i<pointList.size(); i++) 
	   {
	    Point p = (Point) pointList.get(i);
	    float fx, fy, fz;
	
	    fx = (p.x + Math.abs(min_x));
	    fx/=(max_x+Math.abs(min_x));
	    fx*=127;
	
	    fy = p.y + Math.abs(min_y);
	    fy/=(max_y+Math.abs(min_y));
	    fy*=255;
	
	    fz = (p.z + Math.abs(min_z));
	    fz/=(max_z+Math.abs(min_z));
	    fz*=127;
	
	    p.fx=fx;
	    p.fy=fy;
	    p.fz=fz;
	
	    int ix = (int)Math.floor(fx);
	    int iy = (int)Math.floor(fy);
	    int iz = (int)Math.floor(fz);
	    p.ix=ix;
	    p.iy=iy;
	    p.iz=iz;
	
	    pointMap[ix][iy][iz].add(p);
	
	    //these used to be valuable data structures, but it's better to do
	    //the inverse -- iterate across just 17,000 points, and grab
	    //from the static arrays.  however the ADDRESSES are nice.
	
	    //volume[ix][iy][iz].add(p);
	    //surface[iz][iy].add(p);
	  }
 }
	
	void createMappedPointList(int[][] _channelList, ArrayList<Point> _mappedPointList) {
		  for ( int[] channel : _channelList ) {
		    for ( int cubeNumber : channel ) {
		      if ( cubeNumber == 0 ) {  // if no cube is present at location
		        for (int i=0; i<(16*3*4); i++) { 
		          _mappedPointList.add(zp);
		        }
		      }
		      else {
		        for (Point p: cubes[cubeNumber].getPoints()) { 
		          _mappedPointList.add(p);
		        }
		      }
		    }
		  }
		}
	
	void createFlippedPointList( int[][] _channelList, int[][] _flippedRGBlist, int _mappedPointListSize, ArrayList<Boolean> _masterFlipped ) {
		  
		  ArrayList<Integer> linearChannelList = new ArrayList<Integer>();
		  ArrayList<Integer> flippedStripList = new ArrayList<Integer>();
		  ArrayList<Integer> flippedPointList = new ArrayList<Integer>();
		  
		  // creates linear list of cubes from channel list
		  for ( int[] channel : _channelList ) {
		    for ( int cubeNum : channel ) {
		      linearChannelList.add(cubeNum);
		    }
		  }
		  
		    // creates list of flipped strips
		  for ( int[] cubeInfo : _flippedRGBlist ) {
		    int cubeNumber = cubeInfo[0];    // picks out the cube number
		    if ( linearChannelList.contains(cubeNumber) ) {
		      int indexofcube = linearChannelList.indexOf(cubeNumber);
		      // cycle through strips in list
		      for (int i=1; i<cubeInfo.length; i++) {
		        flippedStripList.add( indexofcube*12 + cubeInfo[i] );     //finds strip number of flipped strip in linear array of strips (starts from 1)
		      }
		    }
		  }

		  // creates list of flipped points
		  for ( int stripNum: flippedStripList ) {
		    for (int pointNumInStrip = 1; pointNumInStrip <= 16; pointNumInStrip++) {
		      flippedPointList.add( 16*(stripNum-1) + pointNumInStrip );    //adds all flipped points to a list, starts from point 1 on strip 1
		    }
		  }
		  

		  //creates a boolean array of size equal to mappedPointList, true means colors RGB, false means BGR
		  for (int i = 0; i < _mappedPointListSize; i++) {
		    if ( flippedPointList.contains(i+1) ) { // do this because flippedPointList starts with point index 1
		      _masterFlipped.add(false);
		    }
		    else {
		      _masterFlipped.add(true);
		    }
		  }  
	}
	void sendToBoards()
	{
	  
	    createAndSendMsg(bone_front, mappedPointListFront, masterFlippedFront);
	    createAndSendMsg(bone_rear, mappedPointListRear, masterFlippedRear);
	  
	}
	
	public byte unsignedByte( int val ) {
		  return (byte)( val > 127 ? val - 256 : val );
		}

	void createAndSendMsg(NetAddress _bone, ArrayList<Point> _mappedPointList, ArrayList<Boolean> _masterFlipped) {
	  int size = 0;
	  int totalsize = 0;
	  int begin = 0;
	  int sum = 0;

	  int cubenum = 0;
	  int clipnum = 0;
	  int stripnum = 0;
	  int pointnum = 0;
	  int msgnum = 0;
	  Point mp = new Point(0, 0, 0);

	  for (int i=0; i<msgarr.length; i++) {
	    msgarr[i] = 0;
	  }


	  for (int i=0; i<_mappedPointList.size(); i++) {
	    Point p = _mappedPointList.get(i);     
	    float nr=0, ng=0, nb=0;

	    // LOGIC TO SEE IF POINT IS RGB FLIPPED
	    if ( _masterFlipped.get(i)==false ) { 
	      //IF FLIPPED, DO THIS
	      mp.r=p.b;
	      mp.g=p.g;
	      mp.b=p.r;
	      //      print("Flipped at linear point: ");println(i);
	    }
	    else {
	      //IF NORMAL, DO THIS
	      mp.r=p.r;
	      mp.g=p.g;
	      mp.b=p.b;
	    }

	    msgarr[size++]=unsignedByte((int)(0));
	    msgarr[size++]=unsignedByte((int)(mp.r*255));
	    msgarr[size++]=unsignedByte((int)(mp.g*255));
	    msgarr[size++]=unsignedByte((int)(mp.b*255));

	    if (size>=msgarr.length || i>=_mappedPointList.size()-1) { // JAAAAAAAAANK
	      msg.clearArguments();
	      msg.add(msgnum++);
	      msg.add(msgarr.length);
	      msg.add(msgarr);
	      try { 
	        oscP5.send(msg, _bone);
	      } 
	      catch (Exception e) {
	      } // ignore
	      totalsize+=msgarr.length;
	      size=0;
	    }
	  }
	}
	public void start()
	{
		oscP5 = new OscP5(this, 9000);
	}
}
