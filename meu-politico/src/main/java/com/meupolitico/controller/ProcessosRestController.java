package com.meupolitico.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RestController
public class ProcessosRestController {

	@ResponseBody
	@RequestMapping(value = "/BuscaMG", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	private void buscaProcessosMinasGerais(String nomePolitico) throws MalformedURLException, IOException {

		// final String link =
		// "http://www4.tjmg.jus.br/juridico/sf/proc_resultado_nome.jsp?tipoPesquisa=2&txtProcesso=&comrCodigo=24"
		// + "&nomePessoa=" + "Fernando%20Damata%20Pimentel"
		// +
		// "&tipoPessoa=X&naturezaProcesso=0&situacaoParte=X&codigoOAB=&tipoOAB=N&ufOAB=MG&numero=20&select=1&tipoConsulta=1&natureza=0&ativoBaixado=X";

		final String link = "http://www4.tjmg.jus.br/juridico/sf/proc_resultado_nome.jsp?tipoPesquisa=2&txtProcesso=&comrCodigo="
				+ 24 + "&nomePessoa=" + nome
				+ "&tipoPessoa=X&naturezaProcesso=0&situacaoParte=X&codigoOAB=&tipoOAB=N&ufOAB=MG&numero=1&select=1&tipoConsulta=1&natureza=0&ativoBaixado=X";

		final String linkTJM_SP = "https://esaj.tjsp.jus.br/cpopg/search.do?conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=NMPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=Alberto+Goldman&uuidCaptcha=";
		try {
			Jsoup.connect(link).timeout(10000).validateTLSCertificates(false).get();
		} catch (Exception e) {
			e.getMessage();
		}

		// SAO PAULO
		Document html = Jsoup.parse(new URL(link).openStream(), "ISO-8859-9", linkTJM_SP);
		Elements form = html.select("table.tabela_formulario");

		ArrayList<String> numerosChaveProcessos = new ArrayList<>();

		for (Element element : form.get(0).getAllElements()) {
			numerosChaveProcessos.add(element.getAllElements().get(3).text().substring(2));
		}

		final String allProcessos = "http://www4.tjmg.jus.br/juridico/sf/proc_resultado2.jsp?pessCodigo="
				+ numerosChaveProcessos.get(0) + "&situacaoParte=X&naturezaProcesso=0" + "&comrCodigo=" + 24
				+ "&numero=" + 20;

		Document html2 = Jsoup.parse(new URL(allProcessos).openStream(), "ISO-8859-9", allProcessos);

	}

	@ResponseBody
	@RequestMapping(value = "/BuscaSP", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	private void buscaProcessosSaoPaulo(String nomePolitico) throws MalformedURLException, IOException {

		final String linkTJ_SP = "https://esaj.tjsp.jus.br/cpopg/search.do?conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=NMPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta="
				+ "Alberto+Goldman" + "&uuidCaptcha=";

		Document html = Jsoup.connect(linkTJ_SP).validateTLSCertificates(false).post();

		ArrayList<String> linkProcessos = new ArrayList<>();

		Elements elements = html.select("div#listagemDeProcessos");

		Elements elementsProcessos = elements.get(0).getElementsByClass("nuProcesso");

		for (int i = 1; i < elementsProcessos.size(); i++) {
			linkProcessos.add("https://esaj.tjsp.jus.br" + elementsProcessos.get(i).getAllElements().attr("href"));
		}

		Document html_processo = Jsoup.connect(linkProcessos.get(0)).validateTLSCertificates(false).get();

	}

	@ResponseBody
	@RequestMapping(value = "/BuscaRJ", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	private void buscaProcessosRioDeJaneiro() throws MalformedURLException, IOException, InterruptedException {

		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);

		final HtmlPage page = webClient
				.getPage("http://www4.tjrj.jus.br/ConsultaUnificada/consulta.do#tabs-nome-indice1");

		HtmlInput htmlInputNomeParte = (HtmlInput) page.getElementById("nomeParte");
		htmlInputNomeParte.setValueAttribute("Jair Messias Bolsonaro");

		HtmlInput htmlInputAnoInicio = (HtmlInput) page.getElementByName("anoInicio");
		htmlInputAnoInicio.setValueAttribute("30/11/1900");

		// HtmlSelect htmlSelectOrigem = (HtmlSelect)
		// page.getElementByName("origem");
		// htmlSelectOrigem.setSelectedAttribute("2", true);

		HtmlButtonInput htmlButtonInput = (HtmlButtonInput) page.getElementById("pesquisa");
		HtmlPage htmlPage = htmlButtonInput.click();
		webClient.waitForBackgroundJavaScript(7000);
		htmlPage.asText();
	}
}
