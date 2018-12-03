package application;

import com.sun.javafx.geom.Vec2f;

import java.util.ArrayList;
/*
 * Class used to count the clusters of pixels in the image.
 */
public class Cluster {

    private ArrayList<Position> pixelCluster = new ArrayList<>();

    public boolean isNeighbour(Cluster cluster){
        for(Position pixel: cluster.pixelCluster){
            if(isNeighbour(pixel.x, pixel.y)){
                return true;
            }
        }
        return false;
    }
    /*
     * Used in the method to determine the distance between pixels and if they are neighbours.
     */
    public boolean isNeighbour(int x, int y){

        for(Position position: pixelCluster){
            if(Vec2f.distance(position.x, position.y, x ,y) < 2){
              //  System.out.println("two pixels are neighbours - ("+x+", "+y+")"+", ("+position.x+", "+position.y+")");
                return true;
            }
        }
        return false;
    }

    public ArrayList<Position> getPixelCluster() {
        return pixelCluster;
    }

    public void setPixelCluster(ArrayList<Position> pixelCluster) {
        this.pixelCluster = pixelCluster;
    }

}
