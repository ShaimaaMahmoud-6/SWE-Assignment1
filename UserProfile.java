import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private String userName;
    private Map<String,Integer> trackScores;

    public UserProfile(String name){
        this.userName = name;
        trackScores = new HashMap<>();
        trackScores.put("ML/AI", 0);
        trackScores.put("Backend", 0);
        trackScores.put("Front End", 0);
        trackScores.put("Software Engineering", 0);
        trackScores.put("Data Analytics", 0);
    }

    public String getUserName(){
        return userName;
    }
    public int getTrackScore(String track){
        return trackScores.getOrDefault(track, 0);
    }
    public void addPoints(String track, int points){
        int current = trackScores.getOrDefault(track, 0);
        trackScores.put(track, current + points);
    }

    public double getTrackPercentage(String track){
        int totalPoints = 0;
        for(int score : trackScores.values()){
            totalPoints += score;
        }
        int score = trackScores.getOrDefault(track, 0);
        return totalPoints == 0 ? 0 : (score * 100.0) / totalPoints;
    }
    public List<String> getRecommendedTracks(){
        List<String> topTracks = new ArrayList<>();
        int maxPoints = -1;
        for(String track : trackScores.keySet()){
            int score = trackScores.get(track);
            if(score > maxPoints){
                maxPoints = score;
                topTracks.clear();
                topTracks.add(track);
            } else if(score == maxPoints){
                topTracks.add(track);
            }
        }
        return topTracks;
    }
    public String getTrackDetails(String track){
        switch(track){

            case "Software Engineering":
                return "Brief: Builds complete software systems.\n"
                        + "Basics:\n"
                        + "- Programming\n"
                        + "- OOP\n"
                        + "- Data Structures\n"
                        + "- Software Design";

            case "Backend":
                return "Brief: Handles server logic and databases.\n"
                        + "Basics:\n"
                        + "- SQL\n"
                        + "- APIs\n"
                        + "- Server Programming\n"
                        + "- Security";

            case "Front End":
                return "Brief: Designs user interfaces.\n"
                        + "Basics:\n"
                        + "- HTML\n"
                        + "- CSS\n"
                        + "- JavaScript\n"
                        + "- UI/UX";

            case "ML/AI":
                return "Brief: Builds intelligent systems.\n"
                        + "Basics:\n"
                        + "- Python\n"
                        + "- Linear Algebra\n"
                        + "- ML Algorithms\n"
                        + "- Neural Networks";

            case "Data Analytics":
                return "Brief: Analyzes data for insights.\n"
                        + "Basics:\n"
                        + "- Excel / SQL\n"
                        + "- Statistics\n"
                        + "- Visualization\n"
                        + "- Python";

            default:
                return "No details available.";
        }
    }
}


