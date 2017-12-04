package Threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BancoSinSincronizar {

	public static void main(String[] args) {

		Banco b = new Banco();

		for (int i = 0; i < 100; i++) {

			ejecucionTransferencias r = new ejecucionTransferencias(b, i, 2000);

			Thread t = new Thread(r);
			t.start();

		}
	}

}

class Banco {

	private final double[] cuentas;
	private Lock cierreBanco = new ReentrantLock();
	private Condition saldoSuficiente;
	
	public Banco() {

		cuentas = new double[100];

		for (int i = 0; i < cuentas.length; i++) {
			cuentas[i] = 2000;
		}

		saldoSuficiente = cierreBanco.newCondition(); //ledecimos que el cierrebanco va en condicion al saldosuficiente
		
		
	}

	public void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) throws InterruptedException {

		cierreBanco.lock();

		try {

			while (cuentas[cuentaOrigen] < cantidad) { // evalua q el saldo no es inferior a la transferencia

//				System.out.println("-----------CANTIDAD INSUFICIENTE: " + cuentaOrigen + ".... SALDO: "
//						+ cuentas[cuentaOrigen] + "...... " + cantidad);

				saldoSuficiente.await(); //mientras la condicion se cumpla que el thread espere aki hasta que pueda salir de aqui la condicion.
				//es decir que si no tiene bastante dinero para hacer una transferencia que espere hasta tenerlo para hacer esa transferencia.
				
				
				//return; //lo comentamos para que todas se realizen
			}
//				else {
//				System.out.println("----------CANTIDAD OK---------"+ cuentaOrigen);
		
//			}

			System.out.println(Thread.currentThread());

			cuentas[cuentaOrigen] -= cantidad; // dinero que sale de la cuenta de origen

			System.out.printf("%10.2f de %d para %d", cantidad, cuentaOrigen, cuentaDestino);

			cuentas[cuentaDestino] += cantidad;

			System.out.printf(" Saldo total: %10.2f%n", getSaldoTotal());
			
			saldoSuficiente.signalAll(); //despierta a todos los hilos que pudiesen estar a la espera en el await(); para notrificarles que ha realizado todo lo anterior(aver si alguno se le vanta del estado de suspension por lo que ha hecho este hilo)

		} finally {
			// TODO: handle finally clause
			cierreBanco.unlock();
		}
	}

	public double getSaldoTotal() {

		double suma_cuentas = 0;
		for (double a : cuentas) {

			suma_cuentas += a;
		}
		return suma_cuentas;

	}

}

class ejecucionTransferencias implements Runnable {

	private Banco banco;
	private int deLaCuenta;
	private double cantidadMax;

	public ejecucionTransferencias(Banco banco, int deLaCuenta, double cantidadMax) {
		super();
		this.banco = banco;
		this.deLaCuenta = deLaCuenta;
		this.cantidadMax = cantidadMax;
	}

	@Override
	public void run() {

		try {
			while (true) {

				int paraLaCuenta = (int) (100 * Math.random());

				double cantidad = cantidadMax * Math.random();

				banco.transferencia(deLaCuenta, paraLaCuenta, cantidad);

				Thread.sleep((int) (Math.random() * 10));

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
