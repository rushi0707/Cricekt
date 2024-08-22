import java.util.Scanner;

class Team {
    public static final int MAX_PLAYERS = 11;
    
    private String teamName;
    private String[] playerNames;
    private int[] playerRuns;
    private int[] playerStatus;
    private int[] playerBalls;
    private int extraRuns;

    public Team() {
        this.teamName = "";
        this.playerNames = new String[MAX_PLAYERS];
        this.playerRuns = new int[MAX_PLAYERS];
        this.playerStatus = new int[MAX_PLAYERS];
        this.playerBalls = new int[MAX_PLAYERS];
        this.extraRuns = 0;

        for (int i = 0; i < MAX_PLAYERS; i++) {
            this.playerNames[i] = "Player" + (i + 1);
        }
    }

    public void init() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the team name: ");
        this.teamName = scanner.nextLine();
        System.out.println("Enter the number of players: " + MAX_PLAYERS);
        System.out.println("Enter the name of players:");
        for (int i = 0; i < MAX_PLAYERS; i++) {
            System.out.print("Player " + (i + 1) + " Name: ");
            this.playerNames[i] = scanner.nextLine();
            this.playerRuns[i] = 0;
            this.playerStatus[i] = 0;
        }
    }

    public String getName(int i) {
        return playerNames[i];
    }

    public int getRun(int i) {
        return playerRuns[i];
    }

    public int getStatus(int i) {
        return playerStatus[i];
    }

    public int getExtra() {
        return extraRuns;
    }

    public int getBall(int i) {
        return playerBalls[i];
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            total += playerRuns[i];
        }
        return total + extraRuns;
    }

    public void addRuns(int playerNum, int run) {
        playerRuns[playerNum] += run;
    }

    public void setStatus(int player, int status) {
        playerStatus[player] = status;
    }

    public void setOut(int outPlayer, int type, int newPlayer) {
        playerStatus[outPlayer] = type;
        playerStatus[newPlayer] = 1;
    }

    public void setBall(int player) {
        playerBalls[player] += 1;
    }

    public void setExtra(int run) {
        extraRuns += run;
    }
}

class Score extends Team {
    public static final int MAX_OVERS = 50;
    
    private int player1;
    private int player2;
    private int ballCount;
    private int over;
    private int maxOver;
    private int out;
    private int maxOut;
    private int extra;

    public Score() {
        super();
        this.player1 = 0;
        this.player2 = 0;
        this.ballCount = 0;
        this.over = 0;
        this.maxOver = MAX_OVERS;
        this.out = 0;
        this.maxOut = 10;
        this.extra = 0;

        init();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of overs: ");
        this.maxOver = scanner.nextInt();

        System.out.println("Choose opening batsmen:");
        while (true) {
            System.out.print("Player 1: ");
            this.player1 = scanner.nextInt() - 1;
            System.out.print("Player 2: ");
            this.player2 = scanner.nextInt() - 1;

            if (this.player1 < 0 || this.player1 >= MAX_PLAYERS || this.player2 < 0 || this.player2 >= MAX_PLAYERS || this.player1 == this.player2) {
                System.out.println("Invalid entry. Try again.");
            } else {
                setStatus(this.player1, 1);
                setStatus(this.player2, 1);
                break;
            }
        }
    }

    public void show() {
        System.out.println(getTeamName() + " Score:");
        for (int i = 0; i < MAX_PLAYERS; i++) {
            System.out.print(getName(i) + "  " + showStatus(getStatus(i)) + "  " + getRun(i));
            if (getStatus(i) == 1) {
                System.out.print(" (" + getBall(i) + ")");
            }
            System.out.println();
        }
        System.out.println("Extra: " + getExtra());
        System.out.println("Over: " + over + "." + ballCount);
        System.out.println("Wicket: " + out);
        System.out.println("Total score: " + getTotal());

        if (ballCount < 6 && over < maxOver) {
            choose();
        }
    }

    private String showStatus(int i) {
        switch (i) {
            case 0:
                return "";
            case 1:
                return "not out";
            case 2:
                return "bold out";
            case 3:
                return "caught out";
            case 4:
                return "run out";
            default:
                return "unknown";
        }
    }

    private void choose() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose option:");
        System.out.print("Dot Ball 1  Add Run 2  Extra 3  Wicket 4  Exit 10: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                dotBall();
                break;
            case 2:
                addRun();
                break;
            case 3:
                extra1();
                break;
            case 4:
                wicket();
                break;
            case 10:
                return;
            default:
                System.out.println("\nInvalid input");
                dotBall();
                break;
        }
    }

    private void dotBall() {
        System.out.println("Dot Ball");
        ballCount++;
        setBall(player1);
        if (ballCount == 6) {
            overComplete();
        } else {
            show();
        }
    }

    private void extra1() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Extra Run");
        System.out.print("Extra? ");
        extra = scanner.nextInt();
        setExtra(extra);
        show();
    }

    private void addRun() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add Run");
        System.out.print("Runs? ");
        int runs = scanner.nextInt();
        ballCount++;
        setBall(player1);
        addRuns(player1, runs);

        if (runs == 1 || runs == 3) {
            // Change position of batsmen
            int temp = player1;
            player1 = player2;
            player2 = temp;
        }

        if (ballCount == 6) {
            overComplete();
        } else {
            show();
        }
    }

    private void overComplete() {
        System.out.println("Over Complete");
        over++;
        ballCount = 0;
        // Change position of batsmen
        int temp = player1;
        player1 = player2;
        player2 = temp;

        if (over == maxOver) {
            System.out.println("Inning Complete");
            show();
        } else {
            show();
        }
    }

    private void wicket() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wicket");
        System.out.print("Out type? (Bold-1 Caught-2 Run_out-3) ");
        int oType = scanner.nextInt();
        out++;
        ballCount++;
        setBall(player1);

        if (out == maxOut) {
            System.out.println("Inning Complete");
            show();
            return;
        }

        System.out.print("New Batsman's number: ");
        player1 = scanner.nextInt() - 1;
        setOut(player1, oType + 1, player1);

        if (ballCount == 6) {
            overComplete();
        } else {
            show();
        }
    }

    public static void main(String[] args) {
        Score game = new Score();
        game.show();
    }
}
