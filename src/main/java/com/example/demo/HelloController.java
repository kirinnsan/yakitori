package com.example.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class HelloController {

	public static final String OUTPUT_FILE_PATH = "C:\\work_new\\demo\\menuName.csv";

	/**
	 * @return
	 */
	@RequestMapping("/yakitori")
	public String htmlPasrse() {

		try {
			//鳥貴族の焼き鳥メニューページをパース
			Document document = Jsoup.connect("https://www.torikizoku.co.jp/menu/yakitori").get();
			//メニュー名のみ取り出す
			Elements elements = document.select("li h5");

			//メニュー名を格納するリスト生成
			List<String> menuNameList = new ArrayList<String>();

			//メニュー名をリストに追加
			for (Element element : elements) {
				menuNameList.add(element.text());
				System.out.println(element.text());
			}

			//メニュー名をフィルに出力
			outputMenuName(OUTPUT_FILE_PATH, menuNameList);

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
	private void outputMenuName(String path, List<String> menuNameList) {
		FileWriter fw = null;
		BufferedWriter bf = null;
		try {
			//FileWriterを上書き形式で生成
			fw = new FileWriter(path);
			bf = new BufferedWriter(fw);
			if (fw != null) {
				StringBuilder sb = new StringBuilder();
				for (String menuName : menuNameList) {
					sb.append("焼鳥").append(",").append(menuName).append("\n");
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
				if(bf != null) {
					bf.close();
				}
				if(fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
