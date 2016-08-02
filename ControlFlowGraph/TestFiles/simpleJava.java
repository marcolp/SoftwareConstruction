class Simple{
  public static void main(String[] a){
    int x;
    int y;
    y = 0;
    x = 20;
    
    for(int i = 0; i < 10; i++){
      y++;
      y+=1;
      y = y + 1;
    }

    while(x > 10){
      x--;
      x-=1;
      x = x - 1;
    }

    if(x > y){
      x = y;
      x--;
      System.out.println("Hi");
    }
    else if(x < y){
      x+=y;
      System.out.println("If only");
      x++;
    }
    else{
      y--;
      System.out.println("Too hard");
      System.out.println("Hello");
    }
    return x;
  }
}