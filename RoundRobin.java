import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
    }
}

public class RoundRobin {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];

        // Input arrival time and burst times
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1));
            System.out.print("Arrival time: ");
            int arrival = sc.nextInt();
            System.out.print("Burst time: ");
            int burst = sc.nextInt();
            processes[i] = new Process(i + 1, arrival, burst);
        }

        System.out.print("Enter quantum time: ");
        int quantum = sc.nextInt();

        findWaitingTurnaroundCompletionTime(processes, quantum);

        // Calculate averages
        double totalWaitingTime = 0, totalTurnaroundTime = 0;

        System.out.println("\nProcess\tArrival\tBurst\tWaiting\tTurnaround\tCompletion");
        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
            System.out.printf("P%d\t\t%d\t%d\t%d\t%d\t\t%d\n",
                    p.pid, p.arrivalTime, p.burstTime, p.waitingTime, p.turnaroundTime, p.completionTime);
        }

        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWaitingTime / n);
        System.out.printf("Average Turnaround Time = %.2f\n", totalTurnaroundTime / n);

        sc.close();
    }

    static void findWaitingTurnaroundCompletionTime(Process[] processes, int quantum) {
        int n = processes.length;
        int t = 0;  // current time
        int completed = 0;

        boolean[] visited = new boolean[n]; // to track if process is in queue

        Queue<Integer> queue = new LinkedList<>();

        // Sort processes by arrival time for easier management
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        // Start by adding first process(es) that arrive at time 0 or earliest arrival
        for (int i = 0; i < n; i++) {
            if (processes[i].arrivalTime <= t) {
                queue.add(i);
                visited[i] = true;
            }
        }

        // If no process arrives at time 0, jump time to earliest arrival and enqueue
        if (queue.isEmpty()) {
            t = processes[0].arrivalTime;
            queue.add(0);
            visited[0] = true;
        }

        while (completed < n) {
            if (queue.isEmpty()) {
                // If queue empty but processes remain, jump time to next arrival
                for (int i = 0; i < n; i++) {
                    if (!visited[i]) {
                        t = processes[i].arrivalTime;
                        queue.add(i);
                        visited[i] = true;
                        break;
                    }
                }
            }

            int i = queue.poll();
            Process p = processes[i];

            if (p.remainingTime > quantum) {
                t += quantum;
                p.remainingTime -= quantum;
            } else {
                t += p.remainingTime;
                p.remainingTime = 0;
                completed++;

                p.completionTime = t;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
            }

            // Add processes that have arrived till current time and are not visited
            for (int j = 0; j < n; j++) {
                if (!visited[j] && processes[j].arrivalTime <= t) {
                    queue.add(j);
                    visited[j] = true;
                }
            }

            // If current process still has burst left, add it to queue again
            if (p.remainingTime > 0) {
                queue.add(i);
            }
        }
    }
}


