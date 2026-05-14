package controlador;

public class Juego {

   private int[] tablero = new int[9];
   private int turno = 1;
   private boolean hayGanador = false;


   public void marcarCasilla(int posicion){
       if(tablero[posicion] == 0 && !hayGanador){

           tablero[posicion] = turno;

        if (comprobarSiGana()){
            hayGanador = true;
            System.out.println("El jugador" + turno + " ha ganado");
        } else if (tableroLleno()) {
            System.out.println("EMPATE");
        }else{
            turno = (turno == 1) ? 2: 1;
        }
       }
   }
   private boolean comprobarSiGana(){

       return (chequearLinea(0,1,2) || chequearLinea(3,4,5)|| chequearLinea(6,7,8)|| //de izquierda a derecha
               chequearLinea(0,3,6) || chequearLinea(1,4,7)|| chequearLinea(2,5,8)|| // de abajo a arriba
               chequearLinea(0,4,8) || chequearLinea(2,4,6)); // diagonal
   }

   private boolean chequearLinea(int a, int b, int c){
       return tablero[a] != 0 && tablero[a] == tablero[b] && tablero[a] == tablero[c];
   }
   public boolean tableroLleno(){
       for(int i = 0; i < tablero.length; i++) {
           if (tablero[i] == 0) {
               return false;
           }
       }
           return true;

   }

}
