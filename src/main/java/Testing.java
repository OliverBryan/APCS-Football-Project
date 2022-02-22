public class Testing {
    private static int skill() {
        return (int)((Math.pow(10, Math.random()))+0.9);
    }

    public static void main(String[] args) {
        int skill1 = skill();
        int skill2 = skill();
        String qb = "║        QUARTERBACK        ║\n";
        String rb = "║       RUNNING BACK        ║\n";
        String wc = "║       WIDE RECEIVER       ║\n";
        String bar1 = (skill1 != 10)?("║       Touchdown: " + skill1 + "        ║\n"):("║       Touchdown: " + skill1 + "       ║\n");
        String bar2 = (skill2 != 10)?("║         Running: " + skill2 + "        ║\n"):("║         Running: " + skill2 + "       ║\n");
        String box = "╔═══════════════════════════╗\n"+qb+bar1+bar2+
                "╚═══════════════════════════╝\n";
        System.out.println(box);
    }
}
