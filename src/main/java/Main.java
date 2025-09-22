import java.util.Scanner;

enum Action{
    ATTACK, DODGE, DODGE_FAILED;
}

class Unit {
    private int hp;
    private int damage;
    private String name;

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public void damaged(int damage) {
        int hp = getHp() - damage;
        setHp(hp);
        System.out.printf("[%s] : %d 데미지를 입었습니다.\n", getName(), damage);
        if (getHp() <= 0) {
            System.out.printf("[%s] : 현재 남은 체력이 없습니다.\n", getName());
        }
        else{
            System.out.printf("[%s] : 현재 체력은 %d입니다.\n", getName(), getHp());
        }
    }
    public boolean isAlive() {
        if (hp > 0) {
            return true;
        } else {
            return false;
        }
    }
}

class Player extends Unit{
    private final String name; //이름은 수정하지 않음.
    public Player(String name) {
        this.name = name;
        super.setHp((int) (Math.random() * 501) + 500); //500~1000
        super.setDamage((int) (Math.random() * 51) + 50); //50~100
        // Math.random은 Hp와 Damage 따로 호출해야 밸런스 설정 가능
        //ex) 하나의 값을 통일하면 Hp가 높을때 공격력도 높다고 유추 가능
    }
    // 시나리오 클래스에서 이름을 쓸 수 있어서 게터만 남김
    public String getName() {
        return name;
    }
    public Action choice(Action action){
        System.out.printf("Player[%s] 차례\n",name);
        if(action == Action.ATTACK) {
            System.out.printf("Player[%s] <공격> 선택\n",name);
            return Action.ATTACK;
        }
        else{
            System.out.printf("Player[%s] <회피> 선택\n",name);
            return Action.DODGE;
        }
    }
}

class Monster extends Unit{
    private String name;
    public Monster() {
        super.setHp((int) (Math.random() * 301) + 200);
        super.setDamage((int) (Math.random() * 51) + 50);
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public Action choice(){
        //부모 클래스에 abstract Action choice()를 선언하면 Player가 오버로딩이라 추상화 메서드 구현X로 컴파일 에러
        //abstract Action choice(Action action) 선언하면 Monster는 매개변수가 필요없는데 강제로 쓰게 되니 매개변수 무시하기로 함.
        //그런데 이렇게 쓰기엔 매번 이상한 코드가 될 것 같음. 그래서 부모 클래스에 메서드를 안 묶기로 함.
        System.out.printf("Monster[%s] 차례\n",name);
        double random = Math.random();
        if (random < 0.5){
            System.out.printf("Monster[%s] <공격> 선택\n",name);
            return Action.ATTACK; //50프로
        }
        else if (random < 0.7) {
            System.out.printf("Monster[%s] <회피> 선택\n",name);
            System.out.printf("Monster[%s] SUCCESS!!\n",name);
            return Action.DODGE; //20프로
        }
        else  {
            System.out.printf("Monster[%s] <회피> 선택\n",name);
            System.out.printf("Monster[%s] FAILED...\n",name);
            return Action.DODGE_FAILED; //30프로
        }
    }
}

class NormalMonster extends Monster{
    public NormalMonster(String name){
        super.setName(name);
    }
}

class UI{
    private Scanner sc;
    public UI(Scanner sc){
        this.sc = sc;
    }

    public String inputPlayerName(){
        while(true){
            try{
                System.out.print("당신의 이름은?: ");
                String playerName = sc.nextLine(); //next는 한 단어, 문자씩! nextLine은 개행문자 포함 문장 전체!
                if(!playerName.matches("[a-zA-Z가-힣]+")){
                    throw new IllegalArgumentException("문자로만 작성 가능합니다.(숫자, 특수문자 제외)"); //메소드의 전달 인자값이 잘못될 경우 발생
                }
                return playerName;
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public int choice() {
        while (true) {
            System.out.print("1.<공격> 2.<회피> 무엇을 선택하시겠습니까?");
            String choice = sc.nextLine();
            try {
                int num = Integer.parseInt(choice); //숫자로 변환을 시켜주나, 1또는2만 받고 싶기에.
                if (!(num == 1 || num == 2)) {
                    throw new IllegalArgumentException("1 또는 2 입력만 가능합니다.");
                }
                return num;
            } catch (NumberFormatException e) {
                System.out.println(("1 또는 2 입력만 가능합니다."));
            } catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

class Game {
    //Monster monster = new Monster();
    private NormalMonster monster;
    private Player player;
    private boolean gameState = true;
    private int stage = 1;
    private UI ui;
    private Time time;
    public Game(NormalMonster monster, Player player, UI ui){
        this.player = player;
        this.ui = ui;
        this.monster = monster;
    }

    public boolean getGameState() {
        return gameState;
    }
    public void setTime(Time time){
        this.time = time;
    }
    public void startGame(){
        Battle battle = new Battle(player, monster, ui);
        showPlayerInfo();
        while (gameState){
            startStage(battle);
        }
    }
    public void startStage(Battle battle){
        System.out.printf(">>>>>>Stage %d<<<<<<\n", stage);
        System.out.println(monster.getName()+"가 등장했습니다");
        showMonsterInfo();
        int turn = 1;
        while(true){
            System.out.printf("------Turn %d------\n",turn);
            System.out.print(time.getTime());
            battle.startBattle(); //몬스터가 1턴 싸우고 난 뒤 상태
            if(!monster.isAlive()) {
                System.out.println((monster.getName() + " 처치 성공"));
                stage++;
                gameState = false; // 몬스터 개수가 많아지면 처치 후 다음 몬스터 객체 생성하는 걸로 디벨롭!!
                break;
            } else if (!player.isAlive()) {
                System.out.println(player.getName()+" 전투 불가");
                System.out.println("[게임 종료]");
                gameState = false;
                break;
            }
            turn++;
        }
    }
    public void showMonsterInfo(){
        System.out.println("체력: "+monster.getHp());
        System.out.println("공격력: "+monster.getDamage());
    }
    public void showPlayerInfo(){
        System.out.println("체력: "+player.getHp());
        System.out.println("공격력: "+player.getDamage());
    }

}

class Battle{
    private Player player;
    private Monster monster;
    private UI ui;
    public Battle(Player Player, Monster Monster, UI ui){
        this.player = Player;
        this.monster = Monster;
        this.ui = ui;
    }
    public void startBattle(){
        int choice = ui.choice();
        Action playerAction = (choice == 1)?Action.ATTACK:Action.DODGE;
        Action monsterAction = monster.choice();
        fight(player.choice(playerAction),monsterAction);
    }
    public void fight(Action playerAction, Action monsterAction){
        if (playerAction == Action.ATTACK && monsterAction == Action.ATTACK) {
            System.out.printf("Player[%s] Monster[%s] 서로 피해를 입습니다.\n",player.getName(),monster.getName());
            monster.damaged(player.getDamage());
            player.damaged(monster.getDamage());
        } else if (playerAction == Action.ATTACK && monsterAction == Action.DODGE) {
            System.out.printf("Monster[%s] 아무런 피해를 입지 않습니다.\n", monster.getName());
        } else if (playerAction == Action.ATTACK && monsterAction == Action.DODGE_FAILED) {
            System.out.printf("Monster[%s] 회피에 실패하여 피해를 입습니다.\n",monster.getName());
            monster.damaged(player.getDamage());
        } else if (playerAction == Action.DODGE && monsterAction == Action.ATTACK) {
            System.out.printf("Player[%s] 아무런 피해를 입지 않습니다.\n", player.getName());
        } else {
            System.out.println("아무 일도 일어나지 않습니다\n");
        }
    }
}

class Time implements Runnable{
    private Game game;
    private int time=0;

    public Time(Game game){
        this.game = game;
    }
    public String getTime(){
        if (time <= 4){
            return "<SYSTEM>해가 밝게 떠 있는 아침입니다.<SYSTEM>\n";
        }
        else if (time <= 8){
            return "<SYSTEM>곧 해가 질 오후입니다.<SYSTEM>\n";
        }
        else if (time <= 12){
            return "<SYSTEM>해가 지고 어두운 저녁입니다.<SYSTEM>\n";
        }
        else{
            return "<SYSTEM>곧 해가 뜰 새벽입니다.<SYSTEM>\n";
        }
    }
    public void Message() {
        if (time == 3 || time == 7 || time == 11 || time == 15) {
            System.out.print("\n<SYSTEM>시간이 변합니다.<SYSTEM>\n");
        }
    }

    @Override
    public void run(){
        try {
            while (game.getGameState()) {
                Message();
                time++;
                Thread.sleep(1000);
                if(time == 16){
                    time = 0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UI ui = new UI(sc);
        Player player = new Player(ui.inputPlayerName());
        NormalMonster Balrog = new NormalMonster("Balrog");
        Game game = new Game(Balrog,player,ui);//몬스터가 늘어날 때 리스트로 묶어서 넘길 수도 있을까?
        Time time = new Time(game);
        game.setTime(time);
        Thread timeThread = new Thread(time);
        timeThread.start();
        game.startGame();
    }
}
