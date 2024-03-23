import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherApp extends JFrame {
    public WeatherApp(){
        //setup and add a title
        super("Weather App");
        //configure the app to end process once it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        //load the app at the centre of the screen
        setLocationRelativeTo(null);
        //layout manager null to manually position
        setLayout(null);
        setResizable(false);
        addComponents();
    }
   private void addComponents(){
        //search Field
       JTextField searchTextField=new JTextField();

       //set the location
       searchTextField.setBounds(15,15,360,40);
       searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
       add(searchTextField);

       //search button
       JButton searchButton=new JButton(loadImage("src/assets/search.png"));
       searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       searchButton.setBounds(375,13,50,39);
       add(searchButton);

       //weather image
       JLabel weatherConditionImage= new JLabel(loadImage("src/assets/cloudy.png"));
       weatherConditionImage.setBounds(0,125,450,270);
       add(weatherConditionImage);

       //temperature text
       JLabel temperatureText=new JLabel("10 C");
       temperatureText.setBounds(0,350,450,54);
       temperatureText.setFont(new Font("Dialog",Font.BOLD,50));
       temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
       add(temperatureText);

       //weather description
       JLabel weatherDescription=new JLabel("Cloudy");
       weatherDescription.setBounds(0,400,450,35);
       weatherDescription.setFont(new Font("Dialog",Font.BOLD,32));
       weatherDescription.setHorizontalAlignment(SwingConstants.CENTER);
       add(weatherDescription);

       //humidity image
       JLabel humidityImage=new JLabel(loadImage("src/assets/humidity.png"));
       humidityImage.setBounds(15,500,74,66);
       add(humidityImage);

       //humidity text
       JLabel humidityText=new JLabel("<html><b>Humidity</b> 100%</html>");
       humidityText.setBounds(90,500,85,55);
       humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
       add(humidityText);

       //windspeed
       JLabel windSpeed=new JLabel(loadImage("src/assets/windspeed.png"));
       windSpeed.setBounds(220,500,74,66);
       add(windSpeed);

       //windspeed Text
       JLabel windSpeedText=new JLabel("<html><b>Windspeed</b> 15km/h</html>");
       windSpeedText.setBounds(310,500,85,55);
       windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
       add(windSpeedText);
   }
    private ImageIcon loadImage(String resourcePath){
        try{
            BufferedImage image= ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
    } catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Cannot find resource");
        return null;

}
}

