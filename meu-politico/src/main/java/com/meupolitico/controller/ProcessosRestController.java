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

		nomePolitico = "Alberto+Goldman";
		final String linkTJ_SP = "https://esaj.tjsp.jus.br/cpopg/search.do?conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=NMPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta="
				+ nomePolitico + "&uuidCaptcha=";

		Document html = Jsoup.connect(linkTJ_SP).validateTLSCertificates(false).post();

		ArrayList<String> linkProcessos = new ArrayList<String>();

		Elements elements = html.select("div#listagemDeProcessos");

		Elements elementsProcessos = elements.get(0).getElementsByClass("nuProcesso");

		for (int i = 1; i < elementsProcessos.size(); i++) {
			linkProcessos.add("https://esaj.tjsp.jus.br" + elementsProcessos.get(i).getAllElements().attr("href"));
		}

		Politico politico = new Politico();
		politico.setNome(nomePolitico);

		ArrayList<ProcessoJudicial> listaDeProcessos = new ArrayList<ProcessoJudicial>();

		for (String processos : linkProcessos) {

			Document html_processo = Jsoup.connect(processos).validateTLSCertificates(false).post();
			Element detalhesProcesso = html_processo.getElementsByClass("secaoFormBody").get(1);
			ProcessoJudicial processoJudicial = new ProcessoJudicial();
			processoJudicial.setDescricao(detalhesProcesso.getElementsByClass("secaoFormBody").get(0)
					.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text());
			processoJudicial.setNumero(detalhesProcesso.getElementsByClass("secaoFormBody").get(0)
					.getElementsByTag("tr").get(1).getElementsByTag("td").get(0).text());
			processoJudicial.setPolitico(politico);
			listaDeProcessos.add(processoJudicial);

		}

		politico.setProcessosJudiciais(listaDeProcessos);

		return politico;

	}

	@ResponseBody
	@RequestMapping(value = "/BuscaSP", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	private void buscaProcessosSaoPaulo(String nomePolitico) throws MalformedURLException, IOException {

	
		nomePolitico = "Alberto+Goldman";
		final String linkTJ_SP = "https://esaj.tjsp.jus.br/cpopg/search.do?conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=NMPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta="
				+ nomePolitico + "&uuidCaptcha=";

		Document html = Jsoup.connect(linkTJ_SP).validateTLSCertificates(false).post();

		ArrayList<String> linkProcessos = new ArrayList<String>();

		Elements elements = html.select("div#listagemDeProcessos");

		Elements elementsProcessos = elements.get(0).getElementsByClass("nuProcesso");

		for (int i = 1; i < elementsProcessos.size(); i++) {
			linkProcessos.add("https://esaj.tjsp.jus.br" + elementsProcessos.get(i).getAllElements().attr("href"));
		}

		Politico politico = new Politico();
		politico.setNome(nomePolitico);

		ArrayList<ProcessoJudicial> listaDeProcessos = new ArrayList<ProcessoJudicial>();

		for (String processos : linkProcessos) {

			Document html_processo = Jsoup.connect(processos).validateTLSCertificates(false).post();
			Element detalhesProcesso = html_processo.getElementsByClass("secaoFormBody").get(1);
			ProcessoJudicial processoJudicial = new ProcessoJudicial();
			processoJudicial.setDescricao(detalhesProcesso.getElementsByClass("secaoFormBody").get(0)
					.getElementsByTag("tr").get(6).getElementsByTag("td").get(1).text());
			processoJudicial.setNumero(detalhesProcesso.getElementsByClass("secaoFormBody").get(0)
					.getElementsByTag("tr").get(1).getElementsByTag("td").get(0).text());
			processoJudicial.setPolitico(politico);
			listaDeProcessos.add(processoJudicial);
		}

		politico.setProcessosJudiciais(listaDeProcessos);

		return politico;
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
