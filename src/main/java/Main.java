import java.util.Scanner;

abstract class Unit {
    public String name;
    public int hp;
    public int damage;
    Scanner sc = new Scanner(System.in);

    public void damaged(int damage) {
        System.out.println();
        System.out.printf("[%s] : %d 데미지를 입었습니다.\n", name, damage);
        hp -= damage;
        if (hp <= 0){
            System.out.printf("[%s] : 현재 체력은 0입니다.\n", name);
            System.out.printf("[%s] 파괴되었습니다.", name);
            System.exit(0); //main에서도 exit으로 끝이나고 이 함수 호출시 메인메서드가 끝나는 것과 같은 역할 수행
        }
        else{
        System.out.printf("[%s] : 현재 체력은 %d입니다.\n", name, hp);
        }
    }

    public void attack() {

    }

    abstract int randomMade(int sign);

    abstract int choice();
}

class Player extends Unit {
    public Player() {
        super();
        Scanner sc = new Scanner(System.in);
        System.out.print("당신의 이름은?: ");
        name = sc.nextLine();
        System.out.println("초기 능력 랜덤 설정중...");
        hp = randomMade(1); //여기서 this.hp라고 안해도 알아서 오버라이드한 메서드 값을 가져다 쓰는 이유는?
        damage = randomMade(2);
        System.out.printf("%s님 Status\n", name);
        System.out.printf("체 력: %d\n", hp);
        System.out.printf("공격력: %d\n", damage);
    }
    @Override
    int randomMade(int sign) {
        if (sign == 1) {
            hp = (int) (Math.random() * 501) + 500; //500~1000
            return hp;
        } else if (sign == 2) {
            damage = (int) (Math.random() * 51) + 50; //50~100
            return damage;
        }
        return 0;
    }
    @Override
    int choice(){
        System.out.printf("Player[%s] 차례\n",name);
        System.out.print("1.<공격> 2.<회피> 무엇을 선택하시겠습니까?");
        return sc.nextInt();
    }
}

class Monster extends Unit{
    public Monster(){
        super();
    }
    @Override
    int randomMade(int sign) { //메소드를 정의하지 않고 선언만 해서 사용함을 알리고 싶은데 인터페이스를 어떻게 쓰면 되는지?
        if (sign == 1) {
            hp = (int) (Math.random() * 301) + 200; //200~500
            return hp;
        } else if (sign == 2) {
            damage = (int) (Math.random() * 51) + 50; //50~100
            return damage;
        }
        return 0;
    }
    @Override
    int choice(){
        System.out.println();
        System.out.printf("Monster[%s] 차례\n",name);
        System.out.println("1.<공격> 2.<회피> 무엇을 선택하시겠습니까?");
        if ((int)(Math.random() * 10)+1 <= 5){
            return 1;
        }
        else if ((int)(Math.random() * 10)+1 > 5 && (int)(Math.random() * 10)+1 > 5) {
            return 2;
        }
        else  {
            return 3;
        }
    }
}

class Balrog extends Monster {
    public Balrog(){
        super();
        name="Balrog";
        hp = randomMade(1);
        damage = randomMade(2);
        //System.out.printf("%s Status\n",name);
        //System.out.printf("체 력: %d\n",hp);
        //System.out.printf("공격력: %d\n",damage);
    }
    /*
    public void rageOn(){
        if (hp <= hp/2){
            System.out.printf("%s : 체력이 절반 밑으로 떨어져 광폭화를 씁니다.", name);
            damage += 100;
        }
    }

     */
}

class Battle {
    Scanner sc = new Scanner(System.in);
    void start(){
        int choice = 0;
        int num = 0;
        System.out.println("게임시작");
        Unit player = new Player();
        Unit balrog = new Balrog();
        System.out.println("------Round 1------");
        System.out.printf("%s가 등장했습니다.\n",balrog.name);
        while (player.hp > 0 && balrog.hp > 0) {
            System.out.printf("--------%d턴--------\n",++num);
            if (player.choice() == 1) {
                System.out.printf("Player[%s] <공격> 선택\n",player.name);
                choice = balrog.choice();
                if (choice == 1) {
                    System.out.printf("Monster[%s] <공격> 선택\n",balrog.name);
                    balrog.damaged(player.damage);
                    player.damaged(balrog.damage);
                }
                else if (choice == 2) {
                    System.out.printf("Monster[%s] <회피> 선택\n",balrog.name);
                    System.out.println("아무일도 일어나지 않았습니다");
                }
                else{
                    System.out.printf("Monster[%s] <회피> 선택\n",balrog.name);
                    System.out.println("회피에 실패했습니다.");
                    balrog.damaged(player.damage);
                }
            }
            else{
                choice = balrog.choice();
                System.out.printf("Player[%s] <회피> 선택\n",player.name);
                if (choice == 1){
                    System.out.printf("Monster[%s] <공격> 선택\n",balrog.name);
                    System.out.println("아무일도 일어나지 않았습니다");
                }
                else{
                    System.out.printf("Monster[%s] <회피> 선택\n",balrog.name);
                    System.out.println("아무일도 일어나지 않았습니다");
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        //Unit u1 = new Unit();
        Battle battle = new Battle();
        //Monster monster = new Monster();
        //Player player = new Player();
        //Balrog b1 = new Balrog();
        //System.out.println(player.name);
        battle.start();
    }
}
