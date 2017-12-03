package Threads;

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

	public Banco() {

		cuentas = new double[100];

		for (int i = 0; i < cuentas.length; i++) {
			cuentas[i] = 2000;
		}

	}

	public void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) {

		cierreBanco.lock();

		try {

			if (cuentas[cuentaOrigen] < cantidad) { // evalua q el saldo no es inferior a la transferencia

				return;
			}

			System.out.println(Thread.currentThread());

			cuentas[cuentaOrigen] -= cantidad; // dinero que sale de la cuenta de origen

			System.out.printf("%10.2f de %d para %d", cantidad, cuentaOrigen, cuentaDestino);

			cuentas[cuentaDestino] += cantidad;

			System.out.printf(" Saldo total: %10.2f%n", getSaldoTotal());

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
