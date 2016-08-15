package stemmer; /**
 * Created by pc on 25/07/2016.
 */
import edu.udo.cs.wvtool.generic.wordfilter.AbstractStopWordFilter;

import java.util.HashSet;

public class StopWords extends AbstractStopWordFilter{
    private static HashSet m_Stopwords = null;

    private static HashSet m_StopChars = null;

    private static String[] stopWords = new String[] {"ít","src","http","https","á", "à", "ạ", "á_à", "a_ha", "à_ơi", "ạ_ơi", "ai", "ái",
            "ai_ai", "ái_chà", "ái_dà", "ai_nấy", "alô", "a-lô", "amen", "áng", "anh", "ào", "ắt", "ắt_hẳn",
            "ắt_là", "âu_là", "ầu_ơ", "ấy", "bà", "bác", "bài", "bản", "bạn", "bằng", "bằng_ấy", "bằng_không",
            "bằng_nấy", "bao_giờ", "bao_lâu", "bao_nả", "bao_nhiêu", "bập_bà_bập_bõm", "bập_bõm", "bất_kỳ",
            "bất_chợt", "bất_cứ", "bắt_đầu_từ", "bất_đồ", "bất_giác", "bất_kể", "bất_kì", "bất_luận", "bất_nhược",
            "bất_quá", "bất_thình_lình", "bất_tử", "bấy", "bây_bẩy", "bay_biến", "bấy_chầy", "bây_chừ", "bấy_chừ",
            "bây_giờ", "bấy_giờ", "bấy_lâu", "bấy_lâu_nay", "bấy_nay", "bây_nhiêu", "bấy_nhiêu", "bèn", "bển", "béng",
            "bệt", "bị", "biết", "biết_bao", "biết_bao_nhiêu", "biết_chừng_nào", "biết_đâu", "biết_đâu_chừng", "biết_đâu_đấy",
            "biết_mấy", "bớ", "bộ", "bỏ_mẹ", "bởi", "bởi_chưng", "bởi_nhưng", "bội_phần", "bởi_thế", "bởi_vậy", "bởi_vì"
            , "bông", "bỗng", "bỗng_chốc", "bỗng_đâu", "bỗng_dưng", "bỗng_không", "bỗng_nhiên", "bức", "cả", "cả_thảy",
            "cả_thể", "các", "cảm_ơn", "căn", "căn_cắt", "càng", "cật_lực", "cật_sức", "cậu", "cây", "cha", "cha_chả",
            "chắc", "chậc", "chắc_hẳn", "chầm_chập", "chăn_chắn", "chăng", "chẳng_lẽ", "chẳng_những", "chẳng_nữa", "chẳng_phải",
            "chành_chạnh", "chao_ôi", "chết_nỗi", "chết_thật", "chết_tiệt", "chí_chết", "chiếc", "chỉn", "chính", "chính_là",
            "chính_thị", "cho", "chớ", "chớ_chi", "cho_đến", "cho_đến_khi", "cho_nên", "cho_tới", "cho_tới_khi", "choa", "chốc_chốc",
            "chợt", "chú", "chứ", "chu_cha", "chứ_lị", "chú_mày", "chú_mình", "chui_cha", "chủn", "chùn_chùn", "chùn_chũn", "chung_cục",
            "chúng_mình", "chung_qui", "chung_quy", "chung_quy_lại", "chúng_ta", "chúng_tôi", "có", "cô", "cơ", "có_thể", "có_chăng_là",
            "cơ_chừng", "có_dễ", "cơ_hồ", "cổ_lai", "cơ_mà", "cô_mình", "có_vẻ", "cóc_khô", "coi_bộ", "coi_mòi", "con", "còn", "cơn",
            "công_nhiên", "cứ", "cu_cậu", "cứ_việc", "của___", "cực_lực", "cùng", "cũng", "cùng_cực", "cùng_nhau", "cũng_như", "cũng_vậy",
            "cũng_vậy_thôi", "cùng_với", "cuộc", "cuốn", "dạ", "đại_để", "đại_loại", "đại_nhân", "đại_phàm", "dần_dà", "dần_dần", "đang",
            "đáng_lẽ", "đáng_lí", "đáng_lý", "đành_đạch", "đánh_đùng", "dào", "đáo_để", "dẫu", "dầu_sao", "dẫu_sao", "đây", "để", "dễ_sợ",
            "dễ_thường", "đến", "dì", "đi", "điều", "do", "đó", "dở_chừng", "do_đó", "do_vậy", "do_vì", "dữ", "dù_cho", "dù_rằng", "dưới",
            "duy", "em", "gì", "giữa", "hầu_hết", "họ", "hỏi", "khác", "khi", "là", "lại", "làm", "lần", "lên", "luôn", "mà", "mình",
            "mợ", "một", "muốn", "năm", "nào", "này", "nấy", "nên", "nền", "nên_chi", "nếu", "nếu_như", "ngăn_ngắt", "ngay", "ngày",
            "ngay_cả", "ngày_càng", "ngay_khi", "ngay_lập_tức", "ngay_lúc", "ngày_ngày", "ngay_từ", "ngay_tức_khắc", "ngày_xưa",
            "ngày_xửa", "nghe_chừng", "nghe_đâu", "nghen", "nghiễm_nhiên", "nghỉm", "ngõ_hầu", "ngộ_nhỡ", "ngoài", "ngoải", "ngôi",
            "ngọn", "ngọt", "ngươi", "người", "nhà", "nhận", "nhân_dịp", "nhân_tiện", "nhất", "nhất_đán", "nhất_định", "nhất_loạt",
            "nhất_luật", "nhất_mực", "nhất_nhất", "nhất_quyết", "nhất_sinh", "nhất_tâm", "nhất_tề", "nhất_thiết", "nhau", "nhé", "nhỉ",
            "nhiên_hậu", "nhiệt_liệt", "nhỡ_ra", "nhón_nhén", "như", "như_chơi", "như_không", "như_quả", "như_thể", "như_tuồng", "như_vậy",
            "nhưng", "những", "nhưng_mà", "những_ai", "nhung_nhăng", "những_như", "nhược_bằng", "nó", "nọ", "nớ", "nóc", "nói", "nữa",
            "nức_nở", "ồ", "ơ", "ớ", "ờ", "ở", "ở_trên", "ô_hay", "ơ_hay", "ô_hô", "ô_kê", "ô_kìa", "ơ_kìa", "oái", "oai_oái", "ơi",
            "ôi_chao", "ối_dào", "ối_giời", "ối_giời_ơi", "ôi_thôi", "ông", "ổng", "phải", "phải_chăng", "phải_chi", "phăn_phắt",
            "phắt", "phè", "phỉ_phui", "pho", "phóc", "phốc", "phỏng", "phỏng_như", "phót", "phương_chi", "phụt", "phứt", "qua", "quả",
            "quá_chừng", "quá_độ", "quá_đỗi", "quả_đúng", "quá_lắm", "quả_là", "quả_tang", "qua_quít", "qua_quýt", "quá_sá", "quả_thật",
            "quá_thể", "quả_tình", "quá_trời", "quá_ư", "quả_vậy", "quá_xá", "quý_hồ", "quyển", "quyết", "quyết_nhiên", "ra", "ra_phết",
            "ra_trò", "răng", "rằng", "rằng_là", "ráo", "ráo_trọi", "rày", "rén", "ren_rén", "rích", "riêng", "riệt", "riu_ríu", "rồi",
            "rón_rén", "rốt_cục", "rốt_cuộc", "rứa", "rút_cục", "sa_sả____", "_sạch", "sao", "sắp", "sất", "sau", "sau_chót", "sau_cùng",
            "sau_cuối", "sau_đó", "sẽ", "sì", "số", "sở_dĩ", "số_là", "song_le", "sốt_sột", "sự", "suýt", "tà_tà", "tại", "tại_vì", "tấm",
            "tăm_tắp", "tấn", "tanh", "tắp", "tắp_lự", "tất_cả", "tất_tần_tật", "tất_tật", "tất_thảy", "tênh", "thà", "tha_hồ", "thà_là",
            "thà_rằng", "thái_quá", "thậm", "thậm_chí", "than_ôi", "tháng", "thanh", "thành_ra", "thành_thử", "thảo_hèn", "thảo_nào",
            "thật_lực", "thật_ra", "thật_vậy", "thấy", "thẩy", "thế", "thế_à", "thế_là", "thế_mà", "thế_nào", "thế_nên", "thế_ra", "thế_thì"
            , "thếch", "theo", "thì", "thi_thoảng", "thím", "thình_lình", "thỉnh_thoảng", "thoắt", "thoạt", "thoạt_nhiên", "thốc", "thộc",
            "thốc_tháo", "thôi", "thời_gian", "thỏm", "thốt", "thốt_nhiên", "thửa", "thuần", "thực_sự", "thục_mạng", "thực_ra", "thực_vậy",
            "thúng_thắng", "thương_ôi", "tiện_thể", "tiếp_đó", "tiếp_theo", "tít_mù", "tớ", "tỏ_ra", "tò_te", "tỏ_vẻ", "toà", "tốc_tả",
            "toé_khói", "toẹt", "tôi", "tới", "tối_ư", "tông_tốc", "tọt", "tột", "trên", "trển", "trệt", "trếu_tráo", "trệu_trạo",
            "trời_đất_ơi", "trong", "trỏng", "trừ_phi", "trước", "trước_đây", "trước_đó", "trước_kia", "trước_nay", "trước_tiên",
            "từ", "tù_tì", "tự_vì", "tuần_tự", "tức_thì", "tức_tốc", "từng", "tuốt_luốt", "tuốt_tuồn_tuột", "tuốt_tuột", "tựu_trung",
            "tuy", "tuy_nhiên", "tuy_rằng", "tuy_thế", "tuy_vậy", "tuyệt_nhiên", "ư", "ừ", "ử", "ứ_hự", "ứ_ừ", "ủa", "úi", "úi_chà",
            "úi_dào", "và", "vả_chăng", "vả_lại", "vẫn", "vạn_nhất", "vâng", "văng_tê", "vào", "vậy", "vậy_là", "vậy_thì", "về", "veo",
            "vèo", "veo_veo", "vì", "ví_bằng", "vì_chưng", "ví_dù", "ví_phỏng", "vị_tất", "vì_thế", "ví_thử", "vì_vậy", "việc", "vở",
            "vô_hình_trung", "vô_kể", "vô_luận", "vô_vàn", "với", "với_lại", "vốn_dĩ", "vừa_mới", "vung_tán_tàn", "vung_tàn_tán",
            "vung_thiên_địa", "vụt", "xa_xả", "xăm_xăm", "xăm_xắm", "xăm_xúi", "xềnh_xệch", "xệp", "xiết_bao", "xoẳn", "xoành_xoạch",
            "xoét", "xoẹt", "xon_xón", "xuất_kì_bất_ý", "xuất_kỳ_bất_ý", "xuể", "xuống", "ý", "ý_chừng", "ý_da", "cái", "cần", "chỉ",
            "chưa", "chuyện", "của", "đã", "đến_nỗi", "đều", "được", "không", "lúc", "mỗi", "một_cách", "nhiều", "nơi", "rất", "so",
            "vừa", "cao", "quá", "hay", "lớn", "mới", "hơn", "thường", "hoặc", "nh", "ngoài_ra", "hoàn_toàn", "thì_thôi", "ra_sao"
            };
    private static char[] stopChars= new char[]{'(',')',',','.',';','-','+','=','&',':','�',10,'<',39,
            '>','?', '…','“','”','!','"','#','$','{','}','/','*',92, '1','2','3','4','5','6','7','8','9','0','_','[',']'};

    static {
        if (m_Stopwords == null) {
            m_Stopwords = new HashSet();

            for (int i = 0; i < stopWords.length; i++) {
                m_Stopwords.add(stopWords[i]);
            }
        }

    }

    static {
        if (m_StopChars == null){
            m_StopChars= new HashSet();
            for(int i=0; i< stopChars.length; i++){
                m_StopChars.add(stopChars[i]);
            }
        }
    }

    public boolean isStopword(String str) {
        if(str.endsWith(".jpg")) return true;
        else if(str.length()==1) return true;
//        else if(m_StopChars.contains(str.charAt(0))) return true;
        else return m_Stopwords.contains(str.toLowerCase());
    }
}
