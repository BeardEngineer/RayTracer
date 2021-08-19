package ray1.surface;

import ray1.IntersectionRecord;
import ray1.Ray;
import egl.math.Vector2d;
import egl.math.Vector3;
import egl.math.Vector3d;
import ray1.accel.BboxUtils;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Vector3 center = new Vector3();
  public void setCenter(Vector3 center) { this.center.set(center); }
  public Vector3 getCenter() {return this.center.clone();}
  
  /** The radius of the sphere. */
  protected float radius = 1.0f;
  public void setRadius(float radius) { this.radius = radius; }
  public float getRadius() {return this.radius;}
  
  protected final double M_2PI = 2 * Math.PI;
  
  public Sphere() { }
  
  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param ray the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
    // TODO#Ray Task 2: fill in this function.

	    
	    // If there was an intersection, fill out the intersection record
        // p(t) = e + t(s-e)
	 // If discriminant<0, the line of the ray does not intersect the sphere (missed);
	  //If discriminant=0, the line of the ray just touches the sphere in one point (tangent);
	 // If discriminant>0, the line of the ray touches the sphere in two points (intersected).
	  
	  Vector3d rayDir = new Vector3d(rayIn.direction);
	  Vector3d rayOrigin = new Vector3d(rayIn.origin);
	  Vector3d sphereCenter = new Vector3d(this.center);
	  
	  double dotRayDir = rayDir.dot(rayOrigin);
	  double sqrRayDir = rayDir.lenSq();
	  double sqrOrigin = rayOrigin.lenSq();
	  
	  double discriminant = dotRayDir * dotRayDir - sqrRayDir * (sqrOrigin - this.radius*radius);
	  
	  
	  if(discriminant>0) {
		  
		  
		  double t = 0; //default case
		  double t1 = (-1 * rayDir.clone().dot(rayOrigin) - Math.pow(discriminant, 0.5))/rayDir.clone().lenSq(); // subtraction case || t=( -b +- d) /2a
		  double t2 = (-1 * rayDir.clone().dot(rayOrigin) + Math.pow(discriminant, 0.5))/rayDir.clone().lenSq(); // addition case from solution portion of quadratic equation
		  
		  if(t1 > rayIn.start && t1 < rayIn.end) {
			  t = t1;
		  } else if (t2 > rayIn.start && t2 <rayIn.end)
		  {
			  t = t2;
		  }else {
			  return false;
		  }
		  
		  Vector3d location = new Vector3d();
		  rayIn.evaluate(location, t);
		  Vector3d norm = new Vector3d();
		  norm.add(sphereCenter.x, sphereCenter.y, sphereCenter.z).sub(location);
		  norm.negate();
		  double phi = Math.acos(norm.y/this.radius);
		  double theta = Math.atan2(norm.x, norm.z);
		 
		  double xTex = (theta+Math.PI)/(Math.PI*2);
		  double yTex = (Math.PI-phi)/Math.PI;
		  
		  //set outRecord values
		  outRecord.location.set(location);
		  outRecord.normal.set(norm.normalize());
		  outRecord.texCoords.set(new Vector2d(xTex, yTex));
		  outRecord.t = t;
		  outRecord.surface = this;
		  
		  return true;
	  }
	    return false;
  }
  
  /**
   * Compute Bounding Box for sphere
   * */
  public void computeBoundingBox() {
	  BboxUtils.sphereBBox(this);
  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + shader + " end";
  }

}