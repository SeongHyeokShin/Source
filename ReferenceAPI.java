package robot_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.exception.IRException;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QueryParser;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;

public class robot_searchAPI {
	static int adminPORT = 5555; // 서버 PORT
	static int returnCode = 0;
	static String adminIP = "218.145.197.193"; // 서버 IP
	static String highLightStartTag = "<span style=\"color:red;\"><b><u>"; // Highlight tag 설정 startTag
	static String highLightEndTag = "</u></b></span>"; // Highlight tag 설정 endTag
	static QueryParser queryParser = new QueryParser();

	public static void main(String args[]) throws Exception {
		parameterVO parameterVO = new parameterVO();
		//켓 터틀 로봇 고딩교육
		parameterVO.setSearchTerm("로");
		parameterVO.setSearchType("ALL");// 검색 타입 ex) 통합검색 = "ALL", 일반검색 = ""
											// 일반 검색 시, parameterVO.setCollectionName에 해당 컬렉션 값 넣어주기
											// ex) 로봇 제품 = parameterVO.setCollectionName("T_PRODUCT");
		
		parameterVO.setAuto_collectionName("");//자동완성 컬렉션 이름
												//ex) 통합인 경우, 프리마켓인 경우 = ""
												//통합 아닌 경우 ex) 제품마켓 = PRODUCT_AUTO_COMPLETE
		
		parameterVO.setCollectionName("");//해당 관리도구 컬렉션이름 ex) 제품마켓 T_PRODUCT
										// 	기술마켓 T_TECH	
										//	 프리마켓 T_FREEMARKET

		parameterVO.setT_product_order("");// parameter변수값 ex) "SORT_PID", "SORT_UNIT_PRICE", "SORT_SELL_COUNT"
		parameterVO.setT_tech_order("");// parameter변수값 ex) "SORT_T_TECH_TNAME", "SORT_T_TECH_REG_DATE"
		parameterVO.setT_freemarket_order("");// parameter변수값 ex) "SORT_T_TECH_TNAME", "SORT_T_TECH_REG_DATE"

		parameterVO.setT_product_filter("");// parameter변수값 ex)가격 : FILTER_UNIT_PRICE , 날짜 : FILTER_REG_DATE
		parameterVO.setT_tech_filter("");// parameter변수값 ex) 날짜 : FILTER_T_TECH_REG_DATE
		parameterVO.setT_freemarket_filter("");// parameter변수값 ex) 가격 : "FILTER_T_FREEMARKET_SELL_PRICE" , 날짜 :
												// "FILTER_T_FREEMARKET_REG_DATE"
		
		parameterVO.setStart_filter("");// parameter변수값 ex) 가격 :"0" , 날짜 :"20190910"
		parameterVO.setEnd_filter("");// parameter변수값 ex) 가격 :"1000000" , 날짜 : "20190911"

		parameterVO.setProd_big_code("");// parameter변수값 ex) "PCATE_01", "PCATE_02"

		parameterVO.setTech_big_code("");// parameter변수값 ex) "TC_CONTROL", "TC_DESIGN"

		parameterVO.setFree_del_type("");// parameter변수값 ex)"5",
		parameterVO.setFree_category("");// parameter변수값 ex)"FMC_ETC"
		parameterVO.setFree_prd_status(""); // parameter변수값 ex) "U","O","N"
		parameterVO.setFree_deal_method("");// parameter변수값 ex) "M"
		parameterVO.setFree_nego_yn("Y");// parameter변수값 ex) "Y", "N"
		parameterVO.setFree_deal_status("");// parameter변수값 ex) "Y", "N"
		parameterVO.setFree_loc1("");// parameter변수값 ex)"서울특별시","경기도"

		List<Object> Total_list = (List<Object>) searchAPI(parameterVO);
		List<Object> Auto_Complete = getAutoComplete(parameterVO);
		List<Object> Rank_list = rank_service();

		ArrayList<HashMap<String, String>> Auto_arry1 = (ArrayList<HashMap<String, String>>) Auto_Complete.get(0);
		ArrayList<HashMap<String, String>> Auto_arry2 = (ArrayList<HashMap<String, String>>) Auto_Complete.get(1);

		ArrayList<HashMap<String, String>> Arry1 = (ArrayList<HashMap<String, String>>) Total_list.get(0);
		ArrayList<HashMap<String, String>> Arry2 = (ArrayList<HashMap<String, String>>) Total_list.get(1);
		ArrayList<HashMap<String, String>> Arry3 = (ArrayList<HashMap<String, String>>) Total_list.get(2);

		ArrayList<HashMap<String, String>> Rank_Arry = (ArrayList<HashMap<String, String>>) Rank_list.get(0);
		
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Arry1.size(); i++) {
			System.out.println("T_PRODUCT(제품마켓) "+ i + "번째 : " + Arry1.get(i).get("PID"));
			System.out.println("T_PRODUCT(제품마켓) "+ i + "번째 : " + Arry1.get(i).get("PNAME"));
			System.out.println("T_PRODUCT(제품마켓) "+i + "번째 : " + Arry1.get(i).get("REG_DATE"));
			System.out.println("T_PRODUCT(제품마켓) "+i + "번째 : " + Arry1.get(i).get("UNIT_PRICE"));
		}
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Arry2.size(); i++) {
			System.out.println("T_TECH(기술마켓) "+i + "번째 : " + Arry2.get(i).get("T_TECH_TNAME"));
			System.out.println("T_TECH(기술마켓) "+i + "번째 : " + Arry2.get(i).get("T_TECH_CONTENTS"));
			System.out.println("T_TECH(기술마켓) "+i + "번째 : " + Arry2.get(i).get("T_TECH_REG_DATE"));
		}
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Arry3.size(); i++) {
			System.out.println("T_FREEMAKET(프리마켓) "+i + "번째 : " + Arry3.get(i).get("T_FREEMARKET_TITLE"));
			System.out.println("T_FREEMAKET(프리마켓) "+i + "번째 : " + Arry3.get(i).get("T_FREEMARKET_CONTENTS"));
			System.out.println("T_FREEMAKET(프리마켓) "+i + "번째 : " + Arry3.get(i).get("T_FREEMARKET_NEGO_YN"));
		}
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Auto_arry1.size(); i++) {
			System.out.println("Auto_arry1(제품 자동완성) " + i + "번째 :" + Auto_arry1.get(i).get("P_KEYWORD"));

		}
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Auto_arry2.size(); i++) {
			System.out.println("Auto_arry2(기술 자동완성) " + i + "번째 :" + Auto_arry2.get(i).get("T_KEYWORD"));
		}
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for (int i = 0; i < Rank_Arry.size(); i++) {
			System.out.println("Rank_Arry " + i + "번째 :" + Rank_Arry.get(i).get("KEYWORD"));
		}
	}

	public static List<Object> searchAPI(parameterVO parameterVO) {

		Query query = new Query(); // 상담,상담상세 쿼리.
		QuerySet querySet = null;
		SelectSet[] T_PRODUCT_selectSet = null;
		SelectSet[] T_TECH_selectSet = null;
		SelectSet[] T_FREEMARKET_selectSet = null;
		Result[] resultlist = null;
		Result result = null;

		String fieldName = "";
		String value = "";
		List<Object> Total_list = new ArrayList();

		ArrayList<HashMap<String, String>> arry1 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> arry2 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> arry3 = new ArrayList<HashMap<String, String>>();

		int listSize = 10;// 통합 아닐때 뿌려주는 갯수
		if (parameterVO.getSearchType().equals("ALL")) {
			listSize = 3;// 통합 일때 뿌려주는 갯수
		}

		int startIdx = listSize * (parameterVO.getCurrentPage() - 1);
		int endIdx = (parameterVO.getCurrentPage() * listSize) - 1;

		if (!parameterVO.getSearchTerm().equals("")) {

			if (parameterVO.getSearchType().equals("ALL")) {

				querySet = new QuerySet(3);

				for (int i = 0; i < 3; i++) {
					if (i == 0) {
						parameterVO.setCollectionName("T_PRODUCT");
						T_PRODUCT_selectSet = selectMake(parameterVO);
						query = queryMake(parameterVO, startIdx, endIdx);
						querySet.addQuery(query);
						System.out.println("##### T_PRODUCT query " + " : " + queryParser.queryToString(query));

					} else if (i == 1) {
						parameterVO.setCollectionName("T_TECH");
						T_TECH_selectSet = selectMake(parameterVO);
						query = queryMake(parameterVO, startIdx, endIdx);
						querySet.addQuery(query);
						System.out.println("##### T_TECH query " + " : " + queryParser.queryToString(query));
					} else {
						parameterVO.setCollectionName("T_FREEMARKET");
						T_FREEMARKET_selectSet = selectMake(parameterVO);
						query = queryMake(parameterVO, startIdx, endIdx);
						querySet.addQuery(query);
						System.out.println("##### T_FREEMARKET query " + " : " + queryParser.queryToString(query));
					}

				} // 반복문 마지막

			} // 통합 검색 조건문 마지막

			else {// 통합 검색 아닐때
				querySet = new QuerySet(1);
				// System.out.println("collection name : " + parameterVO.getCollectionName());
				if (parameterVO.getCollectionName().equals("T_PRODUCT")) {// 제품마켓
					T_PRODUCT_selectSet = selectMake(parameterVO);
					query = queryMake(parameterVO, startIdx, endIdx);
					querySet.addQuery(query);
					System.out.println("##### T_PRODUCT query " + " : " + queryParser.queryToString(query));
				}

				if (parameterVO.getCollectionName().equals("T_TECH")) {// 기술마켓
					T_TECH_selectSet = selectMake(parameterVO);
					query = queryMake(parameterVO, startIdx, endIdx);
					querySet.addQuery(query);
					System.out.println("##### T_TECH query " + " : " + queryParser.queryToString(query));
				}

				if (parameterVO.getCollectionName().equals("T_FREEMARKET")) {// 프리마켓
					T_FREEMARKET_selectSet = selectMake(parameterVO);
					query = queryMake(parameterVO, startIdx, endIdx);
					querySet.addQuery(query);
					System.out.println("##### T_FREEMARKET query " + " : " + queryParser.queryToString(query));
				}
			} // else마지막

			CommandSearchRequest command = new CommandSearchRequest(adminIP, adminPORT);
			try {
				returnCode = command.request(querySet);
				//System.out.println("returncode : " + returnCode);
				if (returnCode >= 0) {

					ResultSet results = command.getResultSet();// 밑의 반복문에서 사용할 results와 resultlist를 세팅.
					resultlist = results.getResultList();
				} else {
					resultlist = new Result[1];
					resultlist[0] = new Result();
				}
			} catch (IRException e) {
				e.printStackTrace();
			}

			if (parameterVO.getSearchType().equals("ALL")) {
				for (int i = 0; resultlist.length > i; i++) {
					result = resultlist[i];
					// System.out.println("resultlist.length : "+resultlist.length);
					if (i == 0) {
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> MAP1 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {

								fieldName = new String(T_PRODUCT_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								MAP1.put(fieldName, value);
							}
							arry1.add(MAP1);
						} // for문

					} // i==0마지막

					if (i == 1) {
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> MAP2 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {

								fieldName = new String(T_TECH_selectSet[k].getField());
								value = new String(resultlist[i].getResult(j, k));
								MAP2.put(fieldName, value);
							}
							arry2.add(MAP2);
						} // for문
					} // i==1 마지막

					if (i == 2) {
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> MAP3 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {

								fieldName = new String(T_FREEMARKET_selectSet[k].getField());
								value = new String(resultlist[i].getResult(j, k));
								MAP3.put(fieldName, value);
							}
							arry3.add(MAP3);
						} // for문
					} // i==2 마지막

				} // resultlist.length for문 마지막
			} // if마지막

			else {
				for (int i = 0; resultlist.length > i; i++) {
					result = resultlist[i];
					// System.out.println("resultlist.length : "+resultlist.length);
					for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수

						HashMap<String, String> MAP1 = new HashMap<String, String>();
						HashMap<String, String> MAP2 = new HashMap<String, String>();
						HashMap<String, String> MAP3 = new HashMap<String, String>();
						for (int k = 0; k < resultlist[i].getNumField(); k++) {
							// System.out.println("collection name : " + parameterVO.getCollectionName());
							if (parameterVO.getCollectionName().equals("T_PRODUCT")) {

								fieldName = new String(T_PRODUCT_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								MAP1.put(fieldName, value);
							} else if (parameterVO.getCollectionName().equals("T_TECH")) {

								fieldName = new String(T_TECH_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								MAP2.put(fieldName, value);
							} else {

								fieldName = new String(T_FREEMARKET_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								MAP3.put(fieldName, value);
							}

						}
						if (parameterVO.getCollectionName().equals("T_PRODUCT")) {
							arry1.add(MAP1);
						} else if (parameterVO.getCollectionName().equals("T_TECH"))
							arry2.add(MAP2);
						else
							arry3.add(MAP3);
					} // for문
				}
			} // else 마지막

			Total_list.add(arry1);
			Total_list.add(arry2);
			Total_list.add(arry3);
		} // !searchTerm.equals("")조건문 마지막
		return Total_list;
	}

	public static Query queryMake(parameterVO parameterVO, int startIdx, int endIdx) {
		Query query = new Query();
		SelectSet[] selectSet = null;
		WhereSet[] whereSet = null;
		OrderBySet[] orderbys = null;
		FilterSet[] FilterSet = null;

		query.setHighlight(highLightStartTag, highLightEndTag);
		query.setLoggable(true); // 검색 로그 설정(FullLog)
		query.setDebug(true); // Debug 설정
		query.setPrintQuery(true); // PrintQuery 설정

		query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED));// 검색결과 옵션 설정(검색캐쉬, 금지어, 불용어)
		query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));// 동의어, 유의어 확장

		query.setSearchKeyword(parameterVO.getSearchTerm()); // 대표키워드 설정, 카테고리 랭킹 등에서 사용할 키워드 설정
		query.setFrom(parameterVO.getCollectionName()); // From 설정, 검색할 컬렉션을 선택

		selectSet = selectMake(parameterVO);
		whereSet = whereMake(parameterVO);
		orderbys = orderMake(parameterVO);
		FilterSet = filterMake(parameterVO);

		query.setResult(startIdx, endIdx);
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setOrderby(orderbys);
		query.setFilter(FilterSet);
		return query;
	}

	public static SelectSet[] selectMake(parameterVO parameterVO) {
		ArrayList<SelectSet> selectSetList = new ArrayList<SelectSet>();

		if (parameterVO.getCollectionName().equals("T_PRODUCT")) {
			selectSetList.add(new SelectSet("REG_DATE"));
			selectSetList.add(new SelectSet("BIG_CODE"));
			selectSetList.add(new SelectSet("CATEGORY_NAME"));
			selectSetList.add(new SelectSet("MAIN_IMG"));
			selectSetList.add(new SelectSet("MCHT_COMPANY"));
			selectSetList.add(new SelectSet("PID", (byte) (Protocol.SelectSet.HIGHLIGHT)));
			selectSetList.add(new SelectSet("PNAME", (byte) (Protocol.SelectSet.HIGHLIGHT)));
			selectSetList.add(new SelectSet("SELL_COUNT"));
			selectSetList.add(new SelectSet("SHIP_PRICE"));
			selectSetList.add(new SelectSet("UNIT_PRICE"));
			selectSetList.add(new SelectSet("ORG_PRICE"));
		}

		else if (parameterVO.getCollectionName().equals("T_TECH")) {
			selectSetList.add(new SelectSet("T_TECH_TNAME", (byte) (Protocol.SelectSet.HIGHLIGHT)));
			selectSetList.add(new SelectSet("T_TECH_CONTENTS", (byte) (Protocol.SelectSet.HIGHLIGHT | Protocol.SelectSet.SUMMARIZE), 100));
			selectSetList.add(new SelectSet("T_TECH_TID"));
			selectSetList.add(new SelectSet("T_TECH_UUID"));
			selectSetList.add(new SelectSet("T_TECH_MAIN_IMG"));
			selectSetList.add(new SelectSet("T_USER_COMPANY"));
			selectSetList.add(new SelectSet("T_TECH_DEV_COMPANY"));
			selectSetList.add(new SelectSet("T_TECH_KEYWORD"));
			selectSetList.add(new SelectSet("T_TECH_REG_DATE"));
			selectSetList.add(new SelectSet("T_TECH_DEV_NAME"));
			selectSetList.add(new SelectSet("T_TECH_BIG_CODE"));
			selectSetList.add(new SelectSet("T_TECH_APP_FIELD"));
			selectSetList.add(new SelectSet("T_SITE_CD_CD_NAME"));
		}

		else if (parameterVO.getCollectionName().equals("T_FREEMARKET")) {
			selectSetList.add(new SelectSet("T_FREEMARKET_TITLE", (byte) (Protocol.SelectSet.HIGHLIGHT)));
			selectSetList.add(new SelectSet("T_FREEMARKET_CONTENTS", (byte) (Protocol.SelectSet.HIGHLIGHT | Protocol.SelectSet.SUMMARIZE), 100));
			selectSetList.add(new SelectSet("T_FREEMARKET_DEL_TYPE"));
			selectSetList.add(new SelectSet("T_FREEMARKET_CATEGORY"));
			selectSetList.add(new SelectSet("T_FREEMARKET_DEAL_METHOD"));
			selectSetList.add(new SelectSet("T_FREEMARKET_DEAL_STATUS"));
			selectSetList.add(new SelectSet("T_FREEMARKET_DEL_TYPE"));
			selectSetList.add(new SelectSet("T_FREEMARKET_FM_SEQ"));
			selectSetList.add(new SelectSet("T_FREEMARKET_NEGO_YN"));
			selectSetList.add(new SelectSet("T_FREEMARKET_PRD_STATUS"));
			selectSetList.add(new SelectSet("T_FREEMARKET_REG_DATE"));
			selectSetList.add(new SelectSet("T_FREEMARKET_SELL_PRICE"));
			selectSetList.add(new SelectSet("T_LOCATION_LOC1_NAME"));
			selectSetList.add(new SelectSet("T_LOCATION_LOC1_NAME_ALIAS"));
			selectSetList.add(new SelectSet("T_SITE_CD_CD_NAME"));
		}
		SelectSet[] selectSet = new SelectSet[selectSetList.size()];
		for (int i = 0; i < selectSetList.size(); i++) {
			selectSet[i] = ((SelectSet) selectSetList.get(i));
		}
		return selectSet;
	}

	public static WhereSet[] whereMake(parameterVO parameterVO) {

		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

		if (parameterVO.getCollectionName().equals("T_PRODUCT")) {

			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet("IDX_PNAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_PCODE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_FIELD_PCODE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_FIELD_PNAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_UNIT_PRICE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_MCHT_COMPANY", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_SHIP_PRICE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_BIG_CODE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_CATEGORY_NAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			if (!parameterVO.getProd_big_code().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_BIG_CODE", Protocol.WhereSet.OP_HASALL, parameterVO.getProd_big_code()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}

			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		}

		else if (parameterVO.getCollectionName().equals("T_TECH")) {

			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet("IDX_T_TECH_TNAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_FIELD_TECH_TNAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_FIELD_TECH_CONTENTS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_TECH_CONTENTS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_TECH_DEV_COMPANY", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_TECH_APP_FIELD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_TECH_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_USER_COMPANY", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_SITE_CD_CD_NAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_TECH_FILECONTENTS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			if (!parameterVO.getTech_big_code().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_TECH_BIG_CODE", Protocol.WhereSet.OP_HASALL, parameterVO.getTech_big_code()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		}

		else if (parameterVO.getCollectionName().equals("T_FREEMARKET")) {
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet("IDX_T_FREEMARKET_TITLE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_FREEMARKET_CONTENTS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_FIELD_FREEMARKET_TITLE", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_FIELD_FREEMARKET_CONTENTS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_LOCATION_LOC1_NAME", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_T_LOCATION_LOC1_NAME_ALIAS", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm()));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			if (!parameterVO.getFree_del_type().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_DEL_TYPE", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_del_type()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_category().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_PRD_STATUS", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_category()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_prd_status().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_PRD_STATUS", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_prd_status()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_deal_method().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_DEAL_METHOD", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_deal_method()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_nego_yn().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_NEGO_YN", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_nego_yn()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_deal_status().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_DEAL_STATUS", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_deal_status()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

			} else if (!parameterVO.getFree_loc1().equals("")) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet("IDX_T_FREEMARKET_LOC1", Protocol.WhereSet.OP_HASALL, parameterVO.getFree_loc1()));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}

			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));

		}
		WhereSet[] whereSet = new WhereSet[whereSetList.size()];

		for (int i = 0; i < whereSetList.size(); i++) {
			whereSet[i] = ((WhereSet) whereSetList.get(i));
		}
		return whereSet;
	}

	public static FilterSet[] filterMake(parameterVO parameterVO) {
		FilterSet[] filterset = null;

		if (parameterVO.getCollectionName().equals("T_PRODUCT")) {

			if (parameterVO.getT_product_filter().equals("")) {
				parameterVO.setT_product_filter("FILTER_UNIT_PRICE");
				parameterVO.setStart_filter("0");
				parameterVO.setEnd_filter("100000");
			}

			String[] filterKeywords = { parameterVO.getStart_filter(), parameterVO.getEnd_filter() };
			filterset = new FilterSet[] { new FilterSet(Protocol.FilterSet.OP_RANGE, parameterVO.getT_product_filter(), filterKeywords, 1000)
					// op_match : 해당 field의 앞부분이 일치하는 문서에 대하여 결과에 포함.
					// op_range : 해당 범위내의 결과값을 가져옴.
			};// 필터 마지막
		} else if (parameterVO.getCollectionName().equals("T_TECH")) {

			if (parameterVO.getT_tech_filter().equals("")) {
				parameterVO.setT_tech_filter("FILTER_T_TECH_REG_DATE");
				parameterVO.setStart_filter("20190910");
				parameterVO.setEnd_filter("20190910");
			}

			String[] filterKeywords = { parameterVO.getStart_filter(), parameterVO.getEnd_filter() };
			filterset = new FilterSet[] { new FilterSet(Protocol.FilterSet.OP_RANGE, parameterVO.getT_tech_filter(), filterKeywords, 1000)
					// op_match : 해당 field의 앞부분이 일치하는 문서에 대하여 결과에 포함.
					// op_range : 해당 범위내의 결과값을 가져옴.
			};// 필터 마지막
		} else if (parameterVO.getCollectionName().equals("T_FREEMARKET")) {

			if (parameterVO.getT_freemarket_filter().equals("")) {
				parameterVO.setT_freemarket_filter("FILTER_T_FREEMARKET_SELL_PRICE");
				parameterVO.setStart_filter("0");
				parameterVO.setEnd_filter("110000");
			}

			String[] filterKeywords = { parameterVO.getStart_filter(), parameterVO.getEnd_filter() };
			filterset = new FilterSet[] { new FilterSet(Protocol.FilterSet.OP_RANGE, parameterVO.getT_freemarket_filter(), filterKeywords, 1000)
					// op_match : 해당 field의 앞부분이 일치하는 문서에 대하여 결과에 포함.
					// op_range : 해당 범위내의 결과값을 가져옴.
			};// 필터 마지막
		}
		return filterset;
	}

	public static OrderBySet[] orderMake(parameterVO parameterVO) {
		OrderBySet[] orderbys = null;

		if (parameterVO.getCollectionName().equals("T_PRODUCT")) {
			if (parameterVO.getT_product_order().equals(""))// 통합검색 시, 정렬필터의 값이 빈값일경우.
				parameterVO.setT_product_order("SORT_PID");

			orderbys = new OrderBySet[] { new OrderBySet(true, parameterVO.getT_product_order(), Protocol.OrderBySet.OP_POSTWEIGHT) };
		} else if (parameterVO.getCollectionName().equals("T_TECH")) {
			if (parameterVO.getT_tech_order().equals(""))
				parameterVO.setT_tech_order("SORT_T_TECH_TNAME");

			orderbys = new OrderBySet[] { new OrderBySet(true, parameterVO.getT_tech_order(), Protocol.OrderBySet.OP_POSTWEIGHT) };
		} else if (parameterVO.getCollectionName().equals("T_FREEMARKET")) {
			if (parameterVO.getT_freemarket_order().equals(""))
				parameterVO.setT_freemarket_order("SORT_T_FREEMARKET_REG_DATE");

			orderbys = new OrderBySet[] { new OrderBySet(true, parameterVO.getT_freemarket_order(), Protocol.OrderBySet.OP_POSTWEIGHT) };
		}
		return orderbys;

	}
	//검색 부분

	//자동완성 부분
	public static Query AUTO_queryMake(parameterVO parameterVO) {//
		//System.out.println("1111111111111");
		SelectSet[] selectSet = null;
		WhereSet[] whereSet = null;
		Query query = new Query();
		//OrderBySet[] orderbys = null;
		//FilterSet[] FilterSet = null;		
		query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED));

		query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));// 동의어, 유의어 확장

		query.setSearchKeyword(parameterVO.getSearchTerm()); // 대표키워드 설정, 카테고리 랭킹 등에서 사용할 키워드 설정	

		selectSet = Auto_selectMake(parameterVO);
		whereSet = Auto_whereMake(parameterVO);
		query.setFrom(parameterVO.getAuto_collectionName());
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		//query.setOrderby(orderBySet);
		query.setResult(0, 4);
		query.setSearch(true);
		query.setLoggable(false); // 검색 로그 설정(FullLog) //자동완성시, 검색로그가 쌓이면 안되기 때문에 false로 해놓는다.
		query.setDebug(true); // Debug 설정
		query.setPrintQuery(true); // PrintQuery 설정 //개발 시에는 grouplog에서 쿼리를 보기위해 사용. 개발 후에는 false로 바꿔놓아야함.

		return query;
	}

	public static SelectSet[] Auto_selectMake(parameterVO parameterVO) {
		ArrayList<SelectSet> selectSetList = new ArrayList<SelectSet>();
		if (parameterVO.getAuto_collectionName().equals("PRODUCT_AUTO_COMPLETE")) {
			// select
			selectSetList.add(new SelectSet("P_KEYWORD", Protocol.SelectSet.NONE));
			selectSetList.add(new SelectSet("P_COUNT", Protocol.SelectSet.NONE));

		} else if (parameterVO.getAuto_collectionName().equals("TECH_AUTO_COMPLETE")) {
			// select
			selectSetList.add(new SelectSet("T_KEYWORD", Protocol.SelectSet.NONE));
			selectSetList.add(new SelectSet("T_COUNT", Protocol.SelectSet.NONE));

		}
		SelectSet[] selectSet = new SelectSet[selectSetList.size()];
		for (int i = 0; i < selectSetList.size(); i++) {
			selectSet[i] = ((SelectSet) selectSetList.get(i));
		}
		return selectSet;
	}

	public static WhereSet[] Auto_whereMake(parameterVO parameterVO) {
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

		if (parameterVO.getAuto_collectionName().equals("PRODUCT_AUTO_COMPLETE")) {
			// whereset
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet("IDX_P_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_PRE_P_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 60));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_FIELD_P_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		} else if (parameterVO.getAuto_collectionName().equals("TECH_AUTO_COMPLETE")) {
			// whereset
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet("IDX_T_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_PRE_T_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 60));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet("IDX_FIELD_T_KEYWORD", Protocol.WhereSet.OP_HASALL, parameterVO.getSearchTerm(), 50));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		}

		WhereSet[] whereset = new WhereSet[whereSetList.size()];
		for (int i = 0; i < whereSetList.size(); i++) {
			whereset[i] = ((WhereSet) whereSetList.get(i));
		}
		return whereset;
	}

	public static List<Object> getAutoComplete(parameterVO parameterVO) throws Exception {

		Result result = null;
		Result[] resultlist = null;
		String fieldName = "";
		String value = "";
		QuerySet queryset = new QuerySet(1);
		SelectSet[] T_PRODUCT_selectSet = null;
		SelectSet[] T_TECH_selectSet = null;
		List<Object> Auto_totalresult = new ArrayList();
		ArrayList<HashMap<String, String>> arry1 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> arry2 = new ArrayList<HashMap<String, String>>();		
		
			if (parameterVO.getSearchType().equals("ALL")) {
				queryset = new QuerySet(2);
				for (int i = 0; i < 2; i++) {
					if (i == 0) {
						parameterVO.setAuto_collectionName("PRODUCT_AUTO_COMPLETE");
						T_PRODUCT_selectSet = Auto_selectMake(parameterVO);
						queryset.addQuery(AUTO_queryMake(parameterVO));
						System.out.println("##### PRODUCT_AUTO_COMPLETE query " + " : " + queryParser.queryToString(AUTO_queryMake(parameterVO)));
					} else if (i == 1) {
						parameterVO.setAuto_collectionName("TECH_AUTO_COMPLETE");
						T_TECH_selectSet = Auto_selectMake(parameterVO);
						queryset.addQuery(AUTO_queryMake(parameterVO));
						System.out.println("##### TECH_AUTO_COMPLETE query " + " : " + queryParser.queryToString(AUTO_queryMake(parameterVO)));
					}
				}
			} 
			else {
				if (parameterVO.getAuto_collectionName().equals("PRODUCT_AUTO_COMPLETE")) {
					T_PRODUCT_selectSet = Auto_selectMake(parameterVO);
					queryset.addQuery(AUTO_queryMake(parameterVO));
					System.out.println("##### PRODUCT_AUTO_COMPLETE query " + " : " + queryParser.queryToString(AUTO_queryMake(parameterVO)));
				} else if (parameterVO.getAuto_collectionName().equals("TECH_AUTO_COMPLETE")) {
					T_TECH_selectSet = Auto_selectMake(parameterVO);
					queryset.addQuery(AUTO_queryMake(parameterVO));
					System.out.println("##### TECH_AUTO_COMPLETE query " + " : " + queryParser.queryToString(AUTO_queryMake(parameterVO)));
				}
				/*else {
					parameterVO.setAuto_collectionName(auto_collectionName);
				}*/
			}
			if(!parameterVO.getAuto_collectionName().equals("")) {
				CommandSearchRequest command = new CommandSearchRequest(adminIP, adminPORT);
				try {
					returnCode = command.request(queryset);
					//System.out.println("returncode : " + returnCode);
					if (returnCode >= 0) {

						ResultSet results = command.getResultSet();// 밑의 반복문에서 사용할 results와 resultlist를 세팅.
						resultlist = results.getResultList();
					} else {
						resultlist = new Result[1];
						resultlist[0] = new Result();
					}
				} catch (IRException e) {
					e.printStackTrace();
				}
			}
			
			if (parameterVO.getSearchType().equals("ALL")) {
				for (int i = 0; resultlist.length > i; i++) {
					result = resultlist[i];
					if (i == 0) {
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> AUTO_MAP1 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {
								fieldName = new String(T_PRODUCT_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								AUTO_MAP1.put(fieldName, value);
							}
							arry1.add(AUTO_MAP1);
						} // for문	
					} else if (i == 1) {
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> AUTO_MAP2 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {
								fieldName = new String(T_TECH_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								AUTO_MAP2.put(fieldName, value);
							}
							arry2.add(AUTO_MAP2);
						} // for문	
					}
				}

			} else {
				if (parameterVO.getAuto_collectionName().equals("T_PRODUCT")) {
					for (int i = 0; resultlist.length > i; i++) {
						result = resultlist[i];
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> AUTO_MAP1 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {

								fieldName = new String(T_PRODUCT_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								AUTO_MAP1.put(fieldName, value);
							}
							arry1.add(AUTO_MAP1);
						} // for문					
					}
				} else if (parameterVO.getAuto_collectionName().equals("T_TECH")) {
					for (int i = 0; resultlist.length > i; i++) {
						result = resultlist[i];
						for (int j = 0; j < resultlist[i].getRealSize(); j++) {// DQ_DOC의 갯수
							HashMap<String, String> AUTO_MAP2 = new HashMap<String, String>();
							for (int k = 0; k < resultlist[i].getNumField(); k++) {

								fieldName = new String(T_TECH_selectSet[k].getField());
								value = new String(result.getResult(j, k));
								AUTO_MAP2.put(fieldName, value);
								System.out.println(fieldName + ", " + value);
							}
							arry2.add(AUTO_MAP2);
						} // for문	
					}
				}
			}
		
		
		Auto_totalresult.add(arry1);//0
		Auto_totalresult.add(arry2);//1
		return Auto_totalresult;

	}

	public static List<Object> rank_service() throws IRException {//인기검색어 메서드			
		Result[] resultlist;
		String collectionName = "RANKING_KEYWORD"; // 검색 대상 컬렉션
		SelectSet[] getselectSet = null;
		Result result;
		Query query = new Query(); // 전송하기 위한 Query를 설정합니다.
		QuerySet querySet = new QuerySet(1); // Query를 담기 위한 QuerySet 설정
		List<Object> list = new ArrayList();
		ArrayList<HashMap<String, String>> Rank_List = new ArrayList<HashMap<String, String>>();		

		SelectSet[] selectSet = new SelectSet[] { new SelectSet("KEYWORD"), new SelectSet("PREVRANK"), new SelectSet("RANKING"), new SelectSet("REQUESTED"), new SelectSet("TREND_ID") };

		WhereSet[] whereSet = new WhereSet[] { new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN), 
											new WhereSet("IDX_TREND_ID", Protocol.WhereSet.OP_HASALL, "RANKING_KEYWORD", 200), 
											new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE) };

		OrderBySet[] orderbys = new OrderBySet[] { new OrderBySet(true, "SORT_RANKING", Protocol.OrderBySet.OP_POSTWEIGHT) };

		query.setFrom(collectionName); // From 설정, 검색할 컬렉션을 선택
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setOrderby(orderbys);
		query.setDebug(true); // Debug 설정
		query.setPrintQuery(false); // PrintQuery 설정
		query.setResult(0, 4); // 검색 결과의 시작과 끝			
		getselectSet = query.getSelectFields(); // selectset을 얻어옴

		querySet.addQuery(query); // 쿼리 셋에 쿼리 추가				

		CommandSearchRequest command = new CommandSearchRequest(adminIP, adminPORT);
		returnCode = command.request(querySet);
		//System.out.println(returnCode);
		if (returnCode > 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
		} else {
			resultlist = new Result[1];
			resultlist[0] = new Result();
		}

		for (int i = 0; resultlist != null && i < resultlist.length; i++) {
			result = resultlist[i];
			for (int j = 0; j < result.getRealSize(); j++) {// DQ_DOC의 갯수							
				HashMap<String, String> Rank_Hash = new HashMap<String, String>();
				for (int k = 0; k < result.getNumField(); k++) {

					String fieldName = new String(selectSet[k].getField());
					String value = new String(resultlist[i].getResult(j, k));
					
					Rank_Hash.put(fieldName, value);
				}
				Rank_List.add(Rank_Hash);
			}
		}
		list.add(Rank_List);
		return list;
	}

}
