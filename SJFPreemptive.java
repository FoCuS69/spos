import java.util.Scanner;

public class SJFPreemptive {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] burst_time = new int[n];
        int[] arrival_time = new int[n];
        int[] remaining_time = new int[n];
        int[] completion_time = new int[n];
        int[] waiting_time = new int[n];
        int[] turnaround_time = new int[n];

        // Input arrival time and burst time
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1));
            System.out.print("Arrival Time: ");
            arrival_time[i] = sc.nextInt();
            System.out.print("Burst Time: ");
            burst_time[i] = sc.nextInt();
            remaining_time[i] = burst_time[i];
        }

        int completed = 0, currentTime = 0, minIndex = -1;
        int minRemainingTime;
        boolean foundProcess;
        
        // SJF Preemptive algorithm
        while (completed != n) {
            minRemainingTime = Integer.MAX_VALUE;
            foundProcess = false;

            // Find process with minimum remaining time at currentTime
            for (int i = 0; i < n; i++) {
                if (arrival_time[i] <= currentTime && remaining_time[i] > 0 && remaining_time[i] < minRemainingTime) {
                    minRemainingTime = remaining_time[i];
                    minIndex = i;
                    foundProcess = true;
                }
            }

            if (!foundProcess) {
                currentTime++; // If no process is ready, increment time
                continue;
            }

            // Execute the process for 1 unit of time
            remaining_time[minIndex]--;
            currentTime++;

            // If process is completed
            if (remaining_time[minIndex] == 0) {
                completed++;
                completion_time[minIndex] = currentTime;
                turnaround_time[minIndex] = completion_time[minIndex] - arrival_time[minIndex];
                waiting_time[minIndex] = turnaround_time[minIndex] - burst_time[minIndex];
                if (waiting_time[minIndex] < 0) waiting_time[minIndex] = 0;
            }
        }

        // Calculate average waiting and turnaround times
        float totalWaitingTime = 0, totalTurnaroundTime = 0;
        System.out.println("\nProcess\tArrival\tBurst\tCompletion\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            totalWaitingTime += waiting_time[i];
            totalTurnaroundTime += turnaround_time[i];
            System.out.println(
                (i + 1) + "\t" + 
                arrival_time[i] + "\t" + 
                burst_time[i] + "\t" + 
                completion_time[i] + "\t\t" + 
                waiting_time[i] + "\t" + 
                turnaround_time[i]
            );
        }

        System.out.printf("\nAverage Waiting Time: %.2f", (totalWaitingTime / n));
        System.out.printf("\nAverage Turnaround Time: %.2f\n", (totalTurnaroundTime / n));

        sc.close();
    }
}

