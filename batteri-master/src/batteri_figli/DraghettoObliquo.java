package batteri_figli;

/*
  Copyright (C) 2013 Alessandro Bugatti (alessandro.bugatti@istruzione.it)

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'esempio per la gara
 *
 * @author Alessandro Bugatti &lt; alessandro.bugatti@gmail.com &gt;
 */
public class DraghettoObliquo extends batteri.Batterio implements Cloneable {

    /**CARATTERISTICHE:
     * - Controllo ad area con delta 5, se non va a a buon fine spostamento obliquo e situazione successiva
     * - Oppure controllo ad area con delta 100, se non va a buon fine spostamento obliquo e situazione precedente
     * - L'area di movimento è ridotta di 75
     * - Tutto viene svolto in 2 turni differenti
    **/
    
    private int spostamentoX = 1;
    private int spostamentoY = 0;
    private int versoX = (int) (Math.random() * 2) * 2 - 1;
    private int versoY = (int) (Math.random() * 2) * 2 - 1;
    private int situazione = 0;

    public DraghettoObliquo(int x, int y, Color c, batteri.Food f) {
        super(x, y, c, f);
    }

    private boolean controlloVicini(int delta) {
        int xMigliore = x - delta - 10;
        int yMigliore = y - delta - 10;
        int sforzoMigliore = Math.abs(getX() - xMigliore) + Math.abs(getY() - yMigliore);
        for (int i = -delta; i <= delta; i += delta / 5) {
            for (int j = -delta - 1; j <= delta - 1; j += delta / 5) {
                if (ControllaCibo(x + i, y + j)) {
                    int sforzo = Math.abs(getX() - (x + i)) + Math.abs(getY() - (y + j));
                    if (sforzo < sforzoMigliore) {
                        xMigliore = x + i;
                        yMigliore = y + j;
                        sforzoMigliore = Math.abs(getX() - xMigliore) + Math.abs(getY() - yMigliore);
                    }
                }
            }
        }
        if (xMigliore != x - delta - 10) {
            x = xMigliore;
            y = yMigliore;
            return true; 
        } else {
            return false;
        }
    }

    private void spostamento() {
        if ((x + spostamentoX < 75 && versoX == -1) || (x + spostamentoX >= getFoodWitdh() - 75 && versoX == 1)) {
            versoX = -versoX;
        }

        if ((y + spostamentoY < 75 && versoY == -1) || (y + spostamentoY >= getFoodHeight() - 75 && versoY == 1)) {
            versoY = -versoY;
        }

        x += spostamentoX * versoX;
        y += spostamentoY * versoY;

        int temp = spostamentoX;
        spostamentoX = spostamentoY;
        spostamentoY = temp;

    }

    @Override
    protected void Sposta() {

        //Controlla nel 7x7 attorno a lui se c'è del cibo e tiene in memoria la prima posizione in cui l'ha trovato e l'ultima
        //Il batterio segnalerà agli altri 
        if (situazione == 0) {
            boolean risultato = controlloVicini(10);
            if (!risultato) {
                situazione = 1;
                spostamento();
            }
        }
        else if (situazione == 1) {
            boolean risultato = controlloVicini(100);
            if (risultato)
                situazione = 0;
            else 
                spostamento();
        } 
    }

    @Override
    public batteri.Batterio Clona() {
        try {
            return (DraghettoObliquo) this.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DraghettoObliquo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}