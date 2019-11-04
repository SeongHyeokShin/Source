package extension;

import java.sql.SQLException;
import java.util.*;

import com.diquest.ir.dbwatcher.dbcolumn.DbColumnValue;
import com.diquest.ir.dbwatcher.mapper.FieldMapper;

// implements DBExtension interface
public class cho_Keyword_extension {
	
	private final char[] FIRST_CHAR = new char[]{
			'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
		};
	private final String TAB = "\t";//공백용 구분자를 담는 변수 TAB과 WHITESPACE 밑에서 쓰고있진 않음.
	private final String WHITESPACE = "\t";

	// 현재 처리하고 있는 row의 attach 데이타
	private String INITIAL_KEYWORD;

	private static int getIndexOf(String name, DbColumnValue[] columnValue) throws SQLException {
		for (int i=0; i < columnValue.length; i++) {
			if (name.equals(columnValue[i].getName()))
				return i;
		}
		throw new SQLException("DBWatcherExtension : " + name + " 컬럼이 SELECT 되지 않았습니다.");
		//밑의 NameMapperField가쪽에서 에러가 날 경우 select가 되지 않았다는 오류가 이 부분에서 표출되게 끔 생성함.
	}
	
	public FieldMapper getMapper(String fieldName, DbColumnValue[] columnValue) throws SQLException {
		return new NameMapperField(columnValue, fieldName);

	}
	
	private class NameMapperField extends FieldMapper {
		private final int DOCID;		
		private final String fieldName;

		public NameMapperField(DbColumnValue[] columnValue, String fieldName) throws SQLException {
			super(fieldName);
			DOCID = getIndexOf("KEYWORD", columnValue); // 디비의 로우 이름을 설정.
			this.fieldName = fieldName;//필드명 세팅
		}

		public String mapping(DbColumnValue[] value) throws SQLException {
			
			INITIAL_KEYWORD = value[getIndexOf("KEYWORD", value)].getString();//DQ_DOC의 ID값에 해당하는 value값.
			StringBuilder data_fkey = new StringBuilder();//최종적인 결과물을 담아내서 return 하기위한 최종 결과물역할 변수.
			
			if (fieldName.equals("KEYWORD2")) { //Extension을 써서 초성만 담을려고 만든 필드 네임과 같을때만 초성작업을 거치게끔 하기위해
												//만든 조건문.
								
				if(!(INITIAL_KEYWORD.equals("") || INITIAL_KEYWORD == null)){				
					
					StringBuilder F_KEY = new StringBuilder();//임시 저장 문자열.
					
					ArrayList<String> list = new ArrayList<String>();
					
					int intch, first;
					char ch;
					
					int keyword_len = INITIAL_KEYWORD.length();
					String cur_fkey = ""; //임시저장 문자열1
					
					
					for(int i = 0; i < keyword_len; i++) {
						
							ch = INITIAL_KEYWORD.charAt(i);
							intch = (int) ch;
							cur_fkey = F_KEY.toString();
						
						if(Character.isWhitespace(ch)) {//이 문자가 공백인지 아닌지를 구분하는 부분.				
							list.add(WHITESPACE);//공백이면 공백을 리스트에 추가하는 작업.
							continue;
						} 
						
						else {
							
							if(intch < 44032 || intch > 55203) {//44032 한글 유니코드 시작 값 , // 55203한글 유니코드 마지막 값
								list.add(cur_fkey + ch); 		//----> 한글이 아닌경우에 들어오는 조건문 (ex 영어, 숫자 등)
							} 							
							else {//한글일때 들어오는 조건문.
								intch = intch - 44032;//44032 한글 유니코드 시작 값 --->
								first = intch / 588;//588 , 하나의 초성에 구성 가능한 글자수 (21*28)==중성:21,종성28자
								list.add(cur_fkey + FIRST_CHAR[first]);//리스트에 누적해서 담아감. 분류된 초성단어들만.
							}
						}
					}
					if(list.size() > 0){
						data_fkey.append(list.get(0));	
						for(int i = 1; i < list.size(); i++) {					
							data_fkey.append(list.get(i));//list라는 배열안에 담겨있는 초성들을 순서대로 빼온다.							
						}
					}
					
				}
			}		
			return data_fkey.toString();
		}
	
	}
}