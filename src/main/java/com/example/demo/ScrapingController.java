package com.example.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hiros
 *
 */
@RestController
public class ScrapingController {

	/**
	 * @return
	 */
	@RequestMapping("/parse")
	public String htmlPasrse() {

		//項目名のリストを作成
		List<String> menuList = new ArrayList<String>(
				Arrays.asList(settingConst.FAIR, settingConst.KANDOU, settingConst.YAKITORI, settingConst.IPPIN,
						settingConst.SARADA, settingConst.GOHAN_DESSERT, settingConst.DRINK));

		//出力パスのリストを作成
		List<String> outputPathList = new ArrayList<String>(
				Arrays.asList(settingConst.FAIR＿PATH, settingConst.KANDOU＿PATH,
						settingConst.YAKITORI＿PATH, settingConst.IPPIN＿PATH, settingConst.SARADA＿PATH,
						settingConst.GOHAN_DESSERT＿PATH, settingConst.DRINK＿PATH));

		try {

			//パースするページのリスト用意
			List<Document> ParseDocumentList = new ArrayList<Document>();

			//メニューページをパース
			ParseDocumentList.add(Jsoup.connect(settingConst.FAIR_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.KANDOU_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.YAKITORI_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.IPPIN_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.SARADA_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.GOHAN_DESSERT_MENU_URL).get());
			ParseDocumentList.add(Jsoup.connect(settingConst.DRINK_MENU_URL).get());

			//項目名と項目毎メニューのマップを生成
			Map<String, Document> menuMap = new LinkedHashMap<String, Document>();

			for (int i = 0; i < menuList.size(); i++) {
				menuMap.put(menuList.get(i), (ParseDocumentList.get(i)));
			}

			int count = 0;

			for (String key : menuMap.keySet()) {

				//メニュー名を格納するリスト生成
				List<String> menuNameList = new ArrayList<String>();


				//ページの中からメニュー名のみ取り出す
				Elements elements = menuMap.get(key).select("li h5");

				//メニュー名をリストに追加
				for (Element element : elements) {
					menuNameList.add(element.text());
					System.out.println(element.text());
				}

				//メニュー名をファイルに出力
				outputMenuName(key,outputPathList.get(count), menuNameList);

				count++;
			}


			//System.out.println(document.html());
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return "parse finish";
	}

	/**
	 *
	 */
	private void outputMenuName(String key, String path, List<String> menuNameList) {
		FileWriter fw = null;
		BufferedWriter bf = null;
		try {
			//FileWriterを上書き形式で生成
			fw = new FileWriter(path);
			bf = new BufferedWriter(fw);
			if (fw != null) {
				StringBuilder sb = new StringBuilder();
				for (String menuName : menuNameList) {
					sb.append(key).append(",").append(menuName).append("\n");
				}
				//メニュー名をファイルに書き込み
				bf.write(sb.toString());
				bf.flush();
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
