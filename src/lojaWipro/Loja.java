package lojaWipro;

import java.util.Random;
import java.util.Scanner;

public class Loja {

	private static String[][] stock;
	private static String[][] clienteCart;
	private static int numberItemsInCart = 0;

	public static String[][] loadStock() {

		String[][] products = new String[10][4];
		String[] productsName = { "Café", "Arroz", "Feijão", "Margarina", "Queijo" };
		int minimumStock = 5;
		int maximumStock = 100;
		float minimumPrice = 1f;
		float maximumPrice = 20f;
		float randomPrice;

		Random random = new Random();

		if (productsName.length > 0) {
			for (int i = 0; i < productsName.length; i++) {
				products[i][0] = Integer.toString(i + 1); // Código do Produto
				products[i][1] = productsName[i]; // Nome do Produto
				// qnt no estoque
				products[i][2] = Integer.toString((random.nextInt(maximumStock - minimumStock) + 1) + minimumStock);
				randomPrice = (random.nextFloat() * (maximumPrice - minimumPrice) + minimumPrice);
				products[i][3] = Float.toString(roundValue(randomPrice)); // Preço do produto
			}
		}

		return products;
	}

	public static double calcularValorDoCarrinho(String cart[][]) {
		double valorTotal = 0;
		for (int i = 0; i < 2; i++) {
			valorTotal += Double.parseDouble(cart[i][3]);
		}
		return valorTotal;
	}

	public static void showStock() {

		System.out.println("\t\t\tWipro Store");
		System.out.println("===========================================================\n");
		System.out.println("Código\t\tProduto\t    Qnt.Produtos\tPreço Uni.\n");

		String product;

		for (int i = 0; i < stock.length; i++) {

			if (stock[i][0] != null) {
				for (int j = 0; j < stock[0].length; j++) {
					product = stock[i][j].trim();
					if (j == 1) {

						if (product.length() >= 8) {
							System.out.print(product + "\t");
						} else {
							System.out.print(product + "\t\t");
						}
					} else {
						System.out.print(product + "\t\t");
					}
				}
			} else {
				break;
			}

			System.out.println("\n");
		}
		System.out.println("===========================================================\n");
	}

	// Verificar a quantidade do produto requerido pelo cliente
	public static int amountProduct(int productCode) {
		int amountProduct = Integer.parseInt(stock[productCode - 1][2]);
		return amountProduct;
	}

	// Verificar se um produto existe no estoque
	public static boolean existProduct(int productCode) {
		if (amountProduct(productCode) > 0) {
			return true;
		}
		return false;
	}

	public static String[][] reduzirProdutoNoEstoque(String[][] estoque, int codigoProduto,
			int qntDeSubtracaoDeProduto) {
		int qntDoProduto = Integer.parseInt(estoque[codigoProduto - 1][2]);
		estoque[codigoProduto - 1][2] = Integer.toString(qntDoProduto - qntDeSubtracaoDeProduto);
		return estoque;
	}

	public static String[][] getProduct(int productCode) {
		String[][] product = new String[1][4];
		product[0][0] = stock[productCode - 1][0];
		product[0][1] = stock[productCode - 1][1];
		product[0][2] = stock[productCode - 1][2];
		product[0][3] = stock[productCode - 1][3];

		return product;
	}

	public static float sumItemsFromClientCart() {

		float totalValue = 0;

		for (int i = 0; i < clienteCart.length; i++) {

			if (clienteCart[i][3] != null) {
				totalValue += Float.parseFloat(clienteCart[i][3]);
			} else {
				break;
			}
		}
		return totalValue;
	}

	// Calcular Imposto de 9%
	public static float calculeTax(float totalValueNoTax) {
		float tax = totalValueNoTax * 0.09f;
		return tax;
	}

	public static float roundValue(float value) {
		float roundValue = (float) (Math.round(value * 100.0) / 100.0);
		return roundValue;
	}

	public static int paymentOptions() {

		int option;
		Scanner scanner = new Scanner(System.in);
		System.out.println("\tOpções de Pagamento:");
		System.out.println("\t[1]A vista em dinheiro ou cartão MASTERCARD, recebe 20% de desconto.");
		System.out.println("\t[2]A vista no cartão de crédito, recebe 15% de desconto.");
		System.out.println("\t[3]Em duas vezes, preço normal de etiqueta sem juros.");
		System.out.println("\t[4]Em três vezes, preço normal de etiqueta mais juros de 10%.");
		option = scanner.nextInt();
		scanner.close();
		return option;
	}

	public static void addProductToCart(int productCode, int qntProduct) {

//		addProductToCart(carrinho, nomeDoProduto, qntProductOrdered, priceProductUnit,
//				numDeItensNoCarrinho);

		float priceProductUnit = Float.parseFloat(stock[productCode - 1][3]);

		// soma do preco da quantidade das unidades do mesmo produto escolhido
		float productSum = roundValue(qntProduct * priceProductUnit);

		clienteCart[numberItemsInCart][0] = stock[productCode - 1][1]; // Nome do Produto
		clienteCart[numberItemsInCart][1] = Integer.toString(qntProduct); // Quantidade deste produto no carrinho
		clienteCart[numberItemsInCart][2] = stock[productCode - 1][3]; // Preco de unitario de cada produto
		clienteCart[numberItemsInCart][3] = Float.toString(productSum); // Soma dos precos de cada produto

		// Incrementando o numero de produto no carrinho do cliente
		numberItemsInCart++;

		// diminuido a quantidade de produtos comprados pelo cliente do estoque da loja
		removeProductToStock(productCode, qntProduct);
	}

	public static void showClientCart(float totalAmountNoTax) {

		System.out.println("\t-------------------------------------------------------------");
		System.out.println("\t\t\t    Carrinho de Compras:");
		System.out.print("\t  Nome\t    Qtd. No Carrinho    Preço Unit.     Preço Total\n");
		System.out.println("\t-------------------------------------------------------------");

		for (int i = 0; i < clienteCart.length; i++) {

			if (clienteCart[i][0] != null) {
				if (clienteCart[i][0].length() <= 6) {
					System.out.printf("\t  %s \t\t %s \t\t %s \t\t %s \n", clienteCart[i][0], clienteCart[i][1],
							clienteCart[i][2], clienteCart[i][3]);
				} else {
					System.out.printf("\t  %s \t %s \t\t %s \t\t %s \n", clienteCart[i][0], clienteCart[i][1],
							clienteCart[i][2], clienteCart[i][3]);
				}
			} else {
				break;
			}
		}
		System.out.println("\n\n");
		System.out.println("\t\t\t\t     --------> SubTotal: " + totalAmountNoTax);
		System.out.println("\t-------------------------------------------------------------");
	}

	// Remover produtos do estoque
	public static void removeProductToStock(int productCode, int amountToremove) {
		int qntProdutStock = Integer.parseInt(stock[productCode - 1][2]);
		qntProdutStock -= amountToremove;
		stock[productCode - 1][2] = Integer.toString(qntProdutStock);
	}

	private static void showInvoice(float discount, float totalAmountNoTax, float finalAmountPayable, float tax) {

		System.out.println("Loja Wipro");
		System.out.println("Rua Maristela, nº0 - Supermercado - LTDA");
		System.out.println("CNPJ: 1234567890-00");
		System.out.println();
		System.out.println("\t\t\tNOTA FISCAL");
		System.out.println("============================================================================");
		showClientCart(totalAmountNoTax);
		System.out.println("============================================================================");

		System.out.println("\n\n");
		System.out.printf("DESCONTO NA COMPRA: R$ %.2f %n", discount);
		System.out.printf("VALOR TOTAL A SER PAGO: R$ %.2f %n", finalAmountPayable);
		System.out.printf("VALOR TRIBUTÁRIO: R$ %.2f %n", tax);

	}

	public static void main(String[] args) {

		// carregando o estoque
		stock = loadStock();
		// inicializando o carrinho do cliente com tamanho maximo ao tamanho do estoque
		clienteCart = new String[stock.length][4];

		Scanner scanner = new Scanner(System.in);
		String[][] product;
		int productCode;
		int qntProductOrdered;
		int qntOfOneProductInStock;
		String continua = "s";
		float totalAmountNoTax = 0;
		float totalAmountWithTax = 0;

		do {
			showStock();
			System.out.println("Olá, Digite o código do Produto Desejado: ");
			productCode = scanner.nextInt();

			if (existProduct(productCode)) {
				System.out.println("Insira a Quantidade do Produto Desejado: ");
				qntProductOrdered = scanner.nextInt();

				qntOfOneProductInStock = amountProduct(productCode);

				if (qntOfOneProductInStock >= qntProductOrdered) {
					product = getProduct(productCode);

					System.out.println(qntProductOrdered + " Unidade de " + product[0][1] + " adicionado no carrinho.");

					addProductToCart(productCode, qntProductOrdered);
				} else {
					System.out.println("Desculpe, não temos esta quantidade deste produto, temos no estoque apenas "
							+ qntOfOneProductInStock + " unidades!");
				}

			} else {
				System.out.println("Este Produto está indisponível no Estoque!");
			}

			System.out.println("Deseja continuar comprando? [S/N]: ");
			continua = scanner.next();
			while (true) {
				if (!continua.equalsIgnoreCase("s")) {
					if (!continua.equalsIgnoreCase("n")) {
//						System.out.println(continua);
						System.out.println("Nao Entendi, Deseja continuar Comprando? [S/N]: ");
						continua = scanner.nextLine();
						System.out.println();
						continue;
					}
				}
				break;
			}

		} while (continua.equalsIgnoreCase("s"));

		System.out.println();

		totalAmountNoTax = sumItemsFromClientCart();
		showClientCart(totalAmountNoTax);
		float tax = calculeTax(totalAmountNoTax);
		totalAmountWithTax = totalAmountNoTax + tax;

		System.out.println("\tValor Total da Compra com Imposto de 9%: R$: " + totalAmountWithTax);
		System.out.println();

		int formOfPayment;
		float finalAmountPayable = 0;
		float discount = 0;

		boolean running = true;

		while (running) {
			formOfPayment = paymentOptions();

			switch (formOfPayment) {
			case 1:
				discount = totalAmountNoTax * 0.20f;
				finalAmountPayable = totalAmountNoTax - discount;
				running = false;
				break;
			case 2:
				discount = totalAmountNoTax * 0.15f;
				finalAmountPayable = totalAmountNoTax - discount;
				running = false;
				break;
			case 3:
				discount = 0;
				finalAmountPayable = totalAmountNoTax;
				running = false;
				break;
			case 4:
				finalAmountPayable = totalAmountNoTax + (totalAmountNoTax * 0.10f);
				running = false;
				break;
			default:
				System.out.println("Opção Inválida, Escolha novamente!");
				break;
			}
		}
		tax = calculeTax(finalAmountPayable);
		finalAmountPayable += tax;

		showInvoice(discount, totalAmountNoTax, finalAmountPayable, tax);
		scanner.close();
	}

}
