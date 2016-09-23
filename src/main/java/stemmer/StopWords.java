package stemmer; /**
 * Created by pc on 25/07/2016.
 */
import edu.udo.cs.wvtool.generic.wordfilter.AbstractStopWordFilter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StopWords extends AbstractStopWordFilter{
    private static HashSet m_Stopwords = null;
    public static StopWords instance= null;

    public static StopWords getInstance(){
        if(instance== null) instance= new StopWords();
        return instance;
    }

    public StopWords(){

    }

    private static HashSet m_StopChars = null;

    private static List<String> getStopWords(String path){
        List<String> list= new ArrayList<String>();
        try{
            FileInputStream fileInputStream= new FileInputStream(path);
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(fileInputStream));
            String line="";List<String> words= new ArrayList<String>();
            while ((line=bufferedReader.readLine())!=null){
                for(String s: line.split(" ")){
                    list.add(s);
                }
            }
//            list= new ArrayList<String>(words);
        }catch (IOException e){

        }
        return list;
    }

    private static String[] stopWords = new String[] {"ít","src","http","https","á", "à", "ạ", "á_à", "a_ha", "à_ơi", "ạ_ơi", "ai", "ái",
            "ai_ai", "ái_chà", "ái_dà", "ai_nấy", "alô", "a-lô", "amen", "áng", "anh", "ào", "ắt", "ắt_hẳn",
            "ắt_là", "âu_là", "ầu_ơ", "ấy", "bà", "bác", "bài", "bản", "bạn", "bằng", "bằng_ấy", "bằng_không",
            "bằng_nấy", "bao_giờ", "bao_lâu", "bao_nả", "bao_nhiêu", "bập_bà_bập_bõm", "bập_bõm", "bất_kỳ",
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
            "vừa", "cao", "quá", "hay", "lớn", "mới", "chắc_chắn", "liên_quan", "hơn", "thường", "hoặc", "nh", "ngoài_ra", "hoàn_toàn", "thì_thôi", "ra_sao"
            ,"thứ", "sạch", "gặp", "khoảng", "...", "khiến", "gây", "mọi", "thêm", "cách", "cho_biết", "gần", "nay", "bên", "sử_dụng"
            ,"dùng", "đáng", "nhỏ", "lớn", "đẩy", "kéo", "bỏ", "vô_cùng","thay_đổi", "đồng_thời","đầu", "cuối","chú_ý", "một_số","của", "hai",
            "vàng", "đen", "xanh", "đỏ","ng","th","lời","chúng", "nbsp", "đẩy", "minh", "ta", "đứa", "b.f", "chẳng"
            ,"dẫn", "đủ","đấy","đem", "nhận_ra", "phì","of","re","vân","đồ","đồng","chở","cấp","cựu","lùi","mất","vụ","đưa","nữ","h.","đường"
            ,"haha","yg","loạt","thối","nhìn_chung","tung","mục","chọn","tỏ","hihi","đúng","kỹ","kiểu","hãy","bong","a_ha"
            ,"a-lô","à_ơi","á","à","á_à","ạ","ạ_ơi","ai","ai_ai","ai_nấy","ái","ái_chà","ái_dà","ái_khanh","alô","amen","áng","ào","ắt","ắt_hẳn","ắt_là","âu_là","ầu_ơ","ấy","bài","bán_mạng","bản","bao_giờ","bao_lăm","bao_lâu","bao_nả","bao_nhiêu","bay_biến","bằng","bằng_ấy","bằng_không","bằng_nấy","bắt_đầu_từ","bập_bà_bập_bõm","bập_bõm","bất_chợt","bất_cứ","bất_đồ","bất_giác","bất_kể","bất_kì","bất_kỳ","bất_luận","bất_nhược","bất_quá","bất_thình_lình","bất_tử","bây_bẩy","bây_chừ","bây_giờ","bây_giờ","bây_nhiêu","bấy","bấy_giờ","bấy_chầy","bấy_chừ","bấy_giờ","bấy_lâu","bấy_lâu_nay","bấy_nay","bấy_nhiêu","bèn","béng","bển","bệt","biết_bao","biết_bao_nhiêu","biết_chừng_nào","biết_đâu","biết_đâu_chừng","biết_đâu_đấy",
            "biết_mấy","bộ","bội_phần","bông","bỗng","bỗng_chốc","bỗng_dưng","bỗng_đâu","bỗng_không","bỗng_nhiên","bỏ_bố","bỏ_mẹ","bớ",
            "bởi","bởi_chưng","bởi_nhưng","bởi_thế","bởi_vậy","bởi_vì","bức","cả","cả_thảy_","cái","các","cả_thảy","cả_thể","càng",
            "căn","căn_cắt","cật_lực","cật_sức","cây","cha_","cha_chả","chành_chạnh","chao_ôi","chắc","chắc_hẳn","chăn_chắn","chăng",
            "chằn_chặn","chẳng_lẽ","chẳng_những","chẳng_nữa","chẳng_phải","chậc","chầm_chập","chết_nỗi","chết_tiệt","chết_thật",
            "chí_chết","chỉn","chính","chính_là","chính_thị","chỉ","chỉ_do","chỉ_là","chỉ_tại","chỉ_vì","chiếc","cho_biết","cho_đến",
            "cho_đến_khi","cho_nên","cho_tới","cho_tới_khi","choa","chốc_chốc","chớ","chớ_chi","chợt","chú","chu_cha","chú_mày",
            "chú_mình","chui_cha","chùn_chùn","chùn_chũn","chủn","chung_cục","chung_qui","chung_quy","chung_quy_lại","chúng_mình",
            "chúng_ta","chúng_tôi","chứ","chứ_lại","chứ_lị","có_chăng_là","có_dễ","có_vẻ","cóc_khô","coi_bộ","coi_mòi","con",
            "còn","cô","cô_mình","cổ_lai","công_nhiên","cơ","cơ_chừng","cơ_hồ","cơ_mà","cơn","cu_cậu","của","cùng","cùng_cực",
            "cùng_nhau","cùng_với","cũng","cũng_như","cũng_vậy","cũng_vậy_thôi","cứ","cứ_việc","cực_kì","cực_kỳ","cực_lực","cuộc",
            "cuốn","dào","dạ","dần_dà","dần_dần","dầu_sao","dẫu","dẫu_sao","dễ_sợ","dễ_thường","dĩ_chí","do","do_vì","do_đó","do_vậy"
            ,"dở_chừng","dù_cho","dù_rằng","duy","dữ","dưới","đã","đại_để","đại_loại","đại_nhân","đại_phàm","đang","đáng_lẽ","đáng_lí",
            "đáng_lý","đành_đạch","đánh_đùng","đáo_để","đặc_cách","đăm_đắm","đằng_ấy","đâu_đâu","đâu_đây","đâu_đấy","đâu_đó","đầu_tiên"
            ,"đây","đây_đó","đấy_","để_mà","để_cho","đêm_đêm","đến_cùng","đến_đây","đến_nỗi","đều","đều_đều","đi","đích_thị","đó",
            "đó_đây","đoá","đôi_khi","đối_với","đổng","đột_nhiên","đơ","đúng","đúng_là","đúng_như","đúng_thật","đúng_thật_là",
            "đúng_vậy","đùng_đùng","được","eo_ôi","êu","gì","gia_dĩ","giả_như","giả_phỏng","giả_tỉ_như","giả_tỷ_như","giá_mà",
            "giá_mà","giá_như","giá_phỏng","giờ","giờ_đây","giữa","giữa_chừng","ha","hả","hà_rằm","hà_rầm","hay_là","hãy","hắn",
            "hẳn_là","hằng","hẵng","hầu_như","hé","hèn_chi","hèn_gì","hèn_nào","hén","hê","hề","hễ","hềnh_hệch","hết_mình","hết_mực",
            "hết_nước","hết_nước_hết_cái","hết_sảy","hết_sức","hết_thảy","hết_ý","hình_như","hiếm","hiện","hiện_nay","hiện_tại",
            "hò_khoan","hoài_của","hoặc","hoặc_giả","hoặc_là","hồ_dễ","hộc_tốc","hồi_lùng","hốt_nhiên","hơi","hỡi","hỡi_ôi","hòn",
            "hơn","hung","húng_hắng","huống","huống_chi","huống_gì","huống_hồ","huống_nữa","hừ","hử","hứ","hừm","ít","kém","kẻo_mà",
            "kẻo_nữa","kẻo_rồi","kế_đến","kế_tiếp","kha_khá","khá","khả_dĩ","khẩu","khi_nãy","khi_đó","khi_ấy","khi","khi_không",
            "khôn_cùng","khôn_xiết","không","không_dưng","không_khéo","không_mấy_khi","không_những","không_trách","khư_khư","khứ_hồi",
            "khướt","kia","kia_mà","kì_cùng","kì_thật","kì_thực","kì_tình","kìa","kìn_kìn","kỳ_thật","kỳ_thực","kỳ_tình","lá","là",
            "lạ_lùng","lại_thế_nữa","làm_sao","lắm","lắm_lắm","lắm_lúc","lắm_khi","lẳng_lặng","lầm_lụi","làn","lần_hồi","lần_lượt",
            "lập_tức","lâu_lâu","lẻn","lén","leo_lẻo","lẽo_đẽo","lêu","lí_láu","lịa","lóc_cóc","lọc_cọc","long_lóc","lông_lốc","lúc_",
            "lúc_ấy","lúc_bấy_giờ","lúc_đó","lúc_kia","lúc_nãy","lũ_lượt","lui_lủi","lùi_lũi","lùi_lụi","lủi_thủi","luôn","luôn_luôn",
            "luôn_thể","luôn_tiện","lý_láu","mãi","mãi_mãi","mặc_dầu","mặc_dù","mặc_dù_vậy","mặc_nhiên","mặc_sức","mặc_tình","màn","mất",
            "mấy","mầy","mậy","mèm","miễn_là","miễn_sao","min","mình_","mọi","món","mô_Phật","mô_tê","một","một_mực","một_phép",
            "một_số","mỗi","mới","mựa","nào","nào_là","nay","này","nãy","nãy_giờ","năm_thì_mười_hoạ","năng","nằng_nặc","nẫy","nấy",
            "nên_chi","nền","nếu","nếu_như","ngay","ngay_cả","ngay_lập_tức","ngay_lúc","ngay_khi","ngay_từ","ngay_tức_khắc",
            "ngày_càng","ngày_ngày","ngày_xưa","ngày_xửa","ngăn_ngắt","nghe_chừng","nghe_đâu","nghen","nghiễm_nhiên","nghỉm",
            "ngõ_hầu","ngoải","ngoài","ngôi","ngọn","ngọt","ngộ_nhỡ","ngươi","nhau","nhân_dịp","nhân_tiện","nhất","nhất_đán",
            "nhất_định","nhất_loạt","nhất_luật","nhất_mực","nhất_nhất","nhất_quyết","nhất_sinh","nhất_tâm","nhất_tề","nhất_thiết",
            "nhé","nhỉ","nhiên_hậu","nhiệt_liệt","nhón_nhén","nhỡ_ra","nhung_nhăng","như","như_chơi","như_không","như_quả","như_thể",
            "như_tuồng","như_vậy","nhưng","nhưng_mà","những","những_ai","những_như","nhược_bằng","nó","nóc","nọ","nổi","nớ","nữa",
            "nức_nở","oai_oái","oái","ô_hay","ô_hô","ô_kê","ô_kìa","ồ","ôi_chao","ôi_thôi","ối_dào","ối_giời","ối_giời_ơi","ôkê",
            "ổng","ơ","ơ_hay","ơ_kìa","ờ","ớ","ơi","phải_chi","phải_chăng","phăn_phắt","phắt","phè","phỉ_phui","pho","phóc","phỏng",
            "phỏng_như","phót","phốc","phụt","phương_chi","phứt","qua_quít","qua_quýt","quả","quả_đúng","quả_làquả_tang","quả_thật",
            "quả_tình","quả_vậy","quá","quá_chừng","quá_độ","quá_đỗi","quá_lắm","quá_sá","quá_thể","quá_trời","quá_ư","quá_xá","quý_hồ"
            ,"quyển","quyết","quyết_nhiên","ra","ra_phết","ra_trò","ráo","ráo_trọi","rày","răng","rằng","rằng_là","rất","rất_chi_là",
            "rất_đỗi","rất_mực","ren_rén","rén","rích","riệt","riu_ríu","rón_rén","rồi","rốt_cục","rốt_cuộc","rút_cục","rứa","sa_sả",
            "sạch","sao","sau_chót","sau_cùng","sau_cuối","sau_đó","sắp","sất","sẽ","sì","song_le","số_là","sốt_sột","sở_dĩ","suýt",
            "sự","tà_tà","tại","tại_vì","tấm","tấn","tự_vì","tanh","tăm_tắp","tắp","tắp_lự","tất_cả","tất_tần_tật","tất_tật",
            "tất_thảy","tênh","tha_hồ","thà","thà_là","thà_rằng","thái_quá","than_ôi","thanh","thành_ra","thành_thử","thảo_hèn",
            "thảo_nào","thậm","thậm_chí","thật_lực","thật_vậy","thật_ra","thẩy","thế","thế_à","thế_là","thế_mà","thế_nào","thế_nên",
            "thế_ra","thế_thì","thếch","thi_thoảng","thì","thình_lình","thỉnh_thoảng","thoạt","thoạt_nhiên","thoắt","thỏm","thọt",
            "thốc","thốc_tháo","thộc","thôi","thốt","thốt_nhiên","thuần","thục_mạng","thúng_thắng","thửa","thực_ra","thực_vậy",
            "thương_ôi","tiện_thể","tiếp_đó","tiếp_theo","tít_mù","tỏ_ra","tỏ_vẻ","tò_te","toà","toé_khói","toẹt","tọt","tốc_tả",
            "tôi","tối_ư","tông_tốc","tột","tràn_cung_mây","trên","trển","trệt","trếu_tráo","trệu_trạo","trong","trỏng","trời_đất_ơi"
            ,"trừ_phi","tù_tì","tuần_tự","tuốt_luốt","tuốt_tuồn_tuột","tuốt_tuột","tuy","tuy_nhiên","tuy_rằng","tuy_thế","tuy_vậy",
            "tuyệt_nhiên","từng","tức_thì","tức_tốc","tựu_trung","ủa","úi","úi_chà","úi_dào","ư","ứ_hự","ứ_ừ","ử","ừ","và","vả_chăng",
            "vả_lại","vạn_nhất","văng_tê","vẫn","vâng","vậy","vậy_là","vậy_thì","veo","veo_veo","vèo","về","vì","vì_chưng","vì_thế",
            "vì_vậy","ví_bằng","ví_dù","ví_phỏng","ví_thử","vị_tất","vô_hình_trung","vô_kể","vô_luận","vô_vàn","vốn_dĩ","với",
            "với_lại","vở","vung_tàn_tán","vung_tán_tàn","vung_thiên_địa","vụt","vừa_mới","xa_xả","xăm_xăm","xăm_xắm","xăm_xúi",
            "xềnh_xệch","xệp","xiết_bao","xoành_xoạch","xoẳn","xoét","xoẹt","xon_xón","xuất_kì_bất_ý","xuất_kỳ_bất_ý","xuể"
            ,"ý_chừng","ý_da","rã","rõ","mềm","mở","dàn","dính","phần","thấp","cao","mà_còn","mùi","dẽ","khó","chất","tự","thay_vì"
            ,"màu","tróc","tốt","đúng", "sai","chúng","loại", "cũng","gồm","những","để", "như_thế_này","suốt", "vừa_qua","nhằm","tụi","tự","ít_ỏi","dành",
            "tốt","nhìn","nah","buộc","tạo","vài","phát","phép","bổ_sung","dễ_dàng","tiếp_tục","gửi","hay_biết","vân","buổi","đâu","ban","uông"
            ,"mời","đoạn","bé","sống"
            };
    private static char[] stopChars= new char[]{'(',')',',','.',';','-','+','=','&',':','�',10,'<',39,
            '>','?', '…','“','”','!','"','#','$','{','}','/','*',92, '1','2','3','4','5','6','7','8','9','0','_','[',']'};

    static {
        if (m_Stopwords == null) {
            m_Stopwords = new HashSet();
//            ArrayList<String> stopW= getStopWords("")
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
        if(str== null) return true;
        else if(str.endsWith(".jpg")) return true;
        else if(str.length()==1) return true;
        else if (m_StopChars.contains(str.charAt(0))) return true;
        else return m_Stopwords.contains(str.toLowerCase());
    }
}
