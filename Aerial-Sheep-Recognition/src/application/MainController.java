package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;


public class MainController {


	
	public void test(ActionEvent event) {
		System.out.println("test");
	}
	  @FXML
	  private Pane pane ;
	  @FXML private ImageView imageView;
	  //@FXML private Label label;
	  private String imageFile;

	private Image original;
	private WritableImage blackAndWhite;
	//private WritableImage proccessed;

	private ArrayList<Cluster> clusters = new ArrayList<>();
	    
	/*
	* Picks the image and loads it into the Image View
	 */

	public void pickImage (ActionEvent actionEvent) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            imageFile = selectedFile.toURI().toString();
            original = new Image(imageFile);
            imageView.setImage(original);
        } else {
            //label.setText("Image file selection cancelled.");
        }

    }

    /*
    * Changes the Colour of the image loaded into black and white, essentially creates a new image with every pixel changed
    * either into black or white.
    * It counts the clusters of pixels which are touching each other and counts them to determine the number of sheep in the image.
    */
	public void TurnBlackAndWhite(ActionEvent event)throws IOException {
		clusters.clear();

		blackAndWhite= new WritableImage(original.getPixelReader(),(int)original.getWidth(), (int)original.getHeight());

		for(int y = 0; y<original.getHeight(); y++) {
			for (int x = 0; x < original.getWidth(); x++) {

				double blue = blackAndWhite.getPixelReader().getColor(x, y).getBlue();
				double red = blackAndWhite.getPixelReader().getColor(x, y).getRed();
				double green = blackAndWhite.getPixelReader().getColor(x, y).getGreen();


				//Changing the colour of the sheeps in the picture into pure white colour.
				if (Math.abs(blue - red) < 0.2 && Math.abs(blue - green) < 0.2 && Math.abs(red - green) < 0.2 && green > 0.644 && Math.abs(blue - red) < 0.3 && Math.abs(blue - green) < 0.5 && Math.abs(red - green) < 0.1) {
					blackAndWhite.getPixelWriter().setColor(x, y, Color.WHITE);

					boolean hasBeenAdded = false;
					//Adds clusters to the image.
					for(Cluster cluster: clusters){
						if(cluster.isNeighbour(x,y)){
							cluster.getPixelCluster().add(new Position(x,y));
							hasBeenAdded = true;
							break;
						}
					}
					if(!hasBeenAdded){
						Cluster cluster = new Cluster();
						cluster.getPixelCluster().add(new Position(x,y));
						clusters.add(cluster);
					}
				}
				//Sets everything else except the sheep in the image into black colour as the background.
				else{
					blackAndWhite.getPixelWriter().setColor(x, y, Color.BLACK);
				}
			}
		}

		imageView.setImage(blackAndWhite);

		//Calculates the amount of clusters in the image.
		for(int i = 0; i < clusters.size(); i++){
			for(int j = 0; j< clusters.size(); j++){
				if(clusters.get(i) != clusters.get(j) && clusters.get(i).isNeighbour(clusters.get(j))){
					clusters.get(i).getPixelCluster().addAll(clusters.get(j).getPixelCluster());
					clusters.remove(j);
					j--;
				}
			}
		}
		Random rand = new Random();

		//Sets a random generated colour for each cluster of sheeps found in the image.
		for(Cluster cluster: clusters){
			Color color = Color.rgb(Math.abs(rand.nextInt()%255), Math.abs(rand.nextInt()%255), Math.abs(rand.nextInt()%255), 1.0);
			for(Position pos: cluster.getPixelCluster()){
				blackAndWhite.getPixelWriter().setColor(pos.x, pos.y, color);
			}
		}

		System.out.println("Amount of clusters = "+clusters.size());
		countSheep();
	}
	
		//not complete Counter for the amount of sheep
	  public void countSheep() {

	  }

}