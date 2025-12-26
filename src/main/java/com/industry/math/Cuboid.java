package com.industry.math;

public class Cuboid {

    public Vector3 pos1;
    public Vector3 pos2;
    public double lenX, lenY, lenZ;
    public double halfLenX, halfLenY, halfLenZ;
    public Vector3 center;

     public Cuboid(Vector3 pos1, Vector3 pos2) {
         this.pos1 = pos1;
         this.pos2 = pos2;
         this.lenX = Math.abs(pos1.x - pos2.x);
         this.lenY = Math.abs(pos1.y - pos2.y);
         this.lenZ = Math.abs(pos1.z - pos2.z);
         this.halfLenX = lenX / 2;
         this.halfLenY = lenY / 2;
         this.halfLenZ = lenZ / 2;
         this.center = new Vector3((pos1.x + pos2.x) / 2, (pos1.y + pos2.y) / 2, (pos1.z + pos2.z) / 2);
     }
}
