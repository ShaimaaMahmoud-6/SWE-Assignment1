import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        UserProfile user = new UserProfile(name);

        String[] questions = {
                "Do you like to take requests from a person or company and turn them into a product?",
                "Do you enjoy working with data and logic without a visible interface?",
                "Do you enjoy designing interfaces, choosing colors, and arranging elements on web pages or apps?",
                "Do you like to create algorithms that learn and act like a human?",
                "Do you like analyzing data to get results, statistics, and make predictions to improve productivity?"
        };

        String[] tracks = {
                "Software Engineering", "Backend", "Front End", "ML/AI", "Data Analytics"
        };

        for(int i=0;i<questions.length;i++){
            int answer = 0;
            while(true){
                System.out.println(questions[i]);
                System.out.println("1. Yes  2. No");
                answer = scanner.nextInt();
                if(answer == 1 || answer == 2) break;
                System.out.println("Invalid input! Please enter 1 or 2.");
            }
            if(answer == 1){
                user.addPoints(tracks[i],1);
            }
        }

        System.out.println("\nTrack Percentages:");
        for(String track: tracks){
            System.out.printf("%s: %.1f%%\n", track, user.getTrackPercentage(track));
        }
        List<String> recommended = user.getRecommendedTracks();

        System.out.println("\nRecommended Track(s): " + String.join(" and ", recommended));

        for(String track : recommended){
            System.out.println("\n========== " + track + " ==========");
            System.out.println(user.getTrackDetails(track));
        }
    }
}
