package com.example.isaretdil;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SozlukActivity extends AppCompatActivity {

    private ConstraintLayout rootLayout;
    private FrameLayout videoPanelContainer;
    private PlayerView playerView;
    private ExoPlayer player;
    private RecyclerView kelimeListesiView;
    private ImageButton btnGeri;
    private EditText aramaCubugu;
    private KelimeAdapter adapter;
    private TextView secilenKelimeBaslik;
    private Button btnSpeed05, btnSpeed10, btnSpeed15;
    private Button aktifHizButonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sozluk);

        rootLayout          = findViewById(R.id.anaConstraintLayout);
        videoPanelContainer = findViewById(R.id.videoPanelContainer);
        playerView          = findViewById(R.id.videoAlani);
        kelimeListesiView   = findViewById(R.id.kelimeListesi);
        btnGeri             = findViewById(R.id.btnGeri);
        aramaCubugu         = findViewById(R.id.aramaCubugu);
        secilenKelimeBaslik = findViewById(R.id.secilenKelimeBaslik);
        btnSpeed05          = findViewById(R.id.btnSpeed05);
        btnSpeed10          = findViewById(R.id.btnSpeed10);
        btnSpeed15          = findViewById(R.id.btnSpeed15);

        aktifHizButonu = btnSpeed10;

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        List<Kelime> kelimeler = new ArrayList<>();
        kelimeler.add(new Kelime("Abla",           "sister",          R.raw.v0_abla));
        kelimeler.add(new Kelime("Acele",          "hurry",           R.raw.v1_acele));
        kelimeler.add(new Kelime("Acıkmak",        "hungry",          R.raw.v2_acikmak));
        kelimeler.add(new Kelime("Afiyet Olsun",   "enjoy your meal", R.raw.v3_afiyet_olsun));
        kelimeler.add(new Kelime("Ağabey",         "brother",         R.raw.v4_agabey));
        kelimeler.add(new Kelime("Ağaç",           "tree",            R.raw.v5_agac));
        kelimeler.add(new Kelime("Ağır",           "heavy",           R.raw.v6_agir));
        kelimeler.add(new Kelime("Ağlamak",        "cry",             R.raw.v7_aglamak));
        kelimeler.add(new Kelime("Aile",           "family",          R.raw.v8_aile));
        kelimeler.add(new Kelime("Akıllı",         "wise",            R.raw.v9_akilli));
        kelimeler.add(new Kelime("Akılsız",        "unwise",          R.raw.v10_akilsiz));
        kelimeler.add(new Kelime("Akraba",         "kin",             R.raw.v11_akraba));
        kelimeler.add(new Kelime("Alışveriş",      "shopping",        R.raw.v12_alisveris));
        kelimeler.add(new Kelime("Anahtar",        "key",             R.raw.v13_anahtar));
        kelimeler.add(new Kelime("Anne",           "mother",          R.raw.v14_anne));
        kelimeler.add(new Kelime("Arkadaş",        "friend",          R.raw.v15_arkadas));
        kelimeler.add(new Kelime("Atatürk",        "ataturk",         R.raw.v16_ataturk));
        kelimeler.add(new Kelime("Ayakkabı",       "shoe",            R.raw.v17_ayakkabi));
        kelimeler.add(new Kelime("Ayna",           "mirror",          R.raw.v18_ayna));
        kelimeler.add(new Kelime("Aynı",           "same",            R.raw.v19_ayni));
        kelimeler.add(new Kelime("Baba",           "father",          R.raw.v20_baba));
        kelimeler.add(new Kelime("Bahçe",          "garden",          R.raw.v21_bahce));
        kelimeler.add(new Kelime("Bakmak",         "look",            R.raw.v22_bakmak));
        kelimeler.add(new Kelime("Bal",            "honey",           R.raw.v23_bal));
        kelimeler.add(new Kelime("Bardak",         "glass",           R.raw.v24_bardak));
        kelimeler.add(new Kelime("Bayrak",         "flag",            R.raw.v25_bayrak));
        kelimeler.add(new Kelime("Bayram",         "feast",           R.raw.v26_bayram));
        kelimeler.add(new Kelime("Bebek",          "baby",            R.raw.v27_bebek));
        kelimeler.add(new Kelime("Bekar",          "single",          R.raw.v28_bekar));
        kelimeler.add(new Kelime("Beklemek",       "wait",            R.raw.v29_beklemek));
        kelimeler.add(new Kelime("Ben",            "I",               R.raw.v30_ben));
        kelimeler.add(new Kelime("Benzin",         "petrol",          R.raw.v31_benzin));
        kelimeler.add(new Kelime("Beraber",        "together",        R.raw.v32_beraber));
        kelimeler.add(new Kelime("Bilgi Vermek",   "inform",          R.raw.v33_bilgi_vermek));
        kelimeler.add(new Kelime("Biz",            "we",              R.raw.v34_biz));
        kelimeler.add(new Kelime("Çalışmak",       "work",            R.raw.v35_calismak));
        kelimeler.add(new Kelime("Çarşamba",       "wednesday",       R.raw.v36_carsamba));
        kelimeler.add(new Kelime("Çatal",          "fork",            R.raw.v37_catal));
        kelimeler.add(new Kelime("Çay",            "tea",             R.raw.v38_cay));
        kelimeler.add(new Kelime("Çaydanlık",      "teapot",          R.raw.v39_caydanlik));
        kelimeler.add(new Kelime("Çekiç",          "hammer",          R.raw.v40_cekic));
        kelimeler.add(new Kelime("Çirkin",         "ugly",            R.raw.v41_cirkin));
        kelimeler.add(new Kelime("Çocuk",          "child",           R.raw.v42_cocuk));
        kelimeler.add(new Kelime("Çorba",          "soup",            R.raw.v43_corba));
        kelimeler.add(new Kelime("Cuma",           "friday",          R.raw.v44_cuma));
        kelimeler.add(new Kelime("Cumartesi",      "saturday",        R.raw.v45_cumartesi));
        kelimeler.add(new Kelime("Cüzdan",         "wallet",          R.raw.v46_cuzdan));
        kelimeler.add(new Kelime("Dakika",         "minute",          R.raw.v47_dakika));
        kelimeler.add(new Kelime("Dede",           "grandfather",     R.raw.v48_dede));
        kelimeler.add(new Kelime("Değiştirmek",    "change",          R.raw.v49_degistirmek));
        kelimeler.add(new Kelime("Devirmek",       "topple",          R.raw.v50_devirmek));
        kelimeler.add(new Kelime("Devlet",         "government",      R.raw.v51_devlet));
        kelimeler.add(new Kelime("Doktor",         "doctor",          R.raw.v52_doktor));
        kelimeler.add(new Kelime("Dolu",           "full",            R.raw.v53_dolu));
        kelimeler.add(new Kelime("Düğün",          "wedding",         R.raw.v54_dugun));
        kelimeler.add(new Kelime("Dün",            "yesterday",       R.raw.v55_dun));
        kelimeler.add(new Kelime("Düşman",         "enemy",           R.raw.v56_dusman));
        kelimeler.add(new Kelime("Duvar",          "wall",            R.raw.v57_duvar));
        kelimeler.add(new Kelime("Eczane",         "pharmacy",        R.raw.v58_eczane));
        kelimeler.add(new Kelime("Eldiven",        "glove",           R.raw.v59_eldiven));
        kelimeler.add(new Kelime("Emek",           "labor",           R.raw.v60_emek));
        kelimeler.add(new Kelime("Emekli",         "retired",         R.raw.v61_emekli));
        kelimeler.add(new Kelime("Erkek",          "male",            R.raw.v62_erkek));
        kelimeler.add(new Kelime("Et",             "meal",            R.raw.v63_et));
        kelimeler.add(new Kelime("Ev",             "house",           R.raw.v64_ev));
        kelimeler.add(new Kelime("Evet",           "yes",             R.raw.v65_evet));
        kelimeler.add(new Kelime("Evli",           "married",         R.raw.v66_evli));
        kelimeler.add(new Kelime("Ezberlemek",     "memorize",        R.raw.v67_ezberlemek));
        kelimeler.add(new Kelime("Fil",            "elephant",        R.raw.v68_fil));
        kelimeler.add(new Kelime("Fotoğraf",       "photograph",      R.raw.v69_fotograf));
        kelimeler.add(new Kelime("Futbol",         "football",        R.raw.v70_futbol));
        kelimeler.add(new Kelime("Geçmiş",         "past",            R.raw.v71_gecmis));
        kelimeler.add(new Kelime("Geçmiş Olsun",  "get well",        R.raw.v72_gecmis_olsun));
        kelimeler.add(new Kelime("Getirmek",       "bring",           R.raw.v73_getirmek));
        kelimeler.add(new Kelime("Göl",            "lake",            R.raw.v74_gol));
        kelimeler.add(new Kelime("Gömlek",         "shirt",           R.raw.v75_gomlek));
        kelimeler.add(new Kelime("Görmek",         "see",             R.raw.v76_gormek));
        kelimeler.add(new Kelime("Göstermek",      "show",            R.raw.v77_gostermek));
        kelimeler.add(new Kelime("Gülmek",         "laugh",           R.raw.v78_gulmek));
        kelimeler.add(new Kelime("Hafif",          "lightweight",     R.raw.v79_hafif));
        kelimeler.add(new Kelime("Haklı",          "right",           R.raw.v80_hakli));
        kelimeler.add(new Kelime("Halı",           "carpet",          R.raw.v81_hali));
        kelimeler.add(new Kelime("Hasta",          "ill",             R.raw.v82_hasta));
        kelimeler.add(new Kelime("Hastane",        "hospital",        R.raw.v83_hastane));
        kelimeler.add(new Kelime("Hata",           "fault",           R.raw.v84_hata));
        kelimeler.add(new Kelime("Havlu",          "towel",           R.raw.v85_havlu));
        kelimeler.add(new Kelime("Hayır",          "no",              R.raw.v86_hayir));
        kelimeler.add(new Kelime("Hayırlı Olsun", "congratulations", R.raw.v87_hayirli_olsun));
        kelimeler.add(new Kelime("Hayvan",         "animal",          R.raw.v88_hayvan));
        kelimeler.add(new Kelime("Hediye",         "gift",            R.raw.v89_hediye));
        kelimeler.add(new Kelime("Helal",          "halal",           R.raw.v90_helal));
        kelimeler.add(new Kelime("Hep",            "always",          R.raw.v91_hep));
        kelimeler.add(new Kelime("Hiç",            "never",           R.raw.v92_hic));
        kelimeler.add(new Kelime("Hoşçakal",       "goodbye",         R.raw.v93_hoscakal));
        kelimeler.add(new Kelime("İçmek",          "drink",           R.raw.v94_icmek));
        kelimeler.add(new Kelime("İğne",           "needle",          R.raw.v95_igne));
        kelimeler.add(new Kelime("İlaç",           "medicine",        R.raw.v96_ilac));
        kelimeler.add(new Kelime("İlgilenmemek",   "not interested",  R.raw.v97_ilgilenmemek));
        kelimeler.add(new Kelime("Işık",           "light",           R.raw.v98_isik));
        kelimeler.add(new Kelime("İtmek",          "push",            R.raw.v99_itmek));
        kelimeler.add(new Kelime("İyi",            "good",            R.raw.v100_iyi));
        kelimeler.add(new Kelime("Kaçmak",         "escape",          R.raw.v101_kacmak));
        kelimeler.add(new Kelime("Kahvaltı",       "breakfast",       R.raw.v102_kahvalti));
        kelimeler.add(new Kelime("Kalem",          "pencil",          R.raw.v103_kalem));
        kelimeler.add(new Kelime("Kalorifer",      "radiator",        R.raw.v104_kalorifer));
        kelimeler.add(new Kelime("Kapı",           "door",            R.raw.v105_kapi));
        kelimeler.add(new Kelime("Kardeş",         "sibling",         R.raw.v106_kardes));
        kelimeler.add(new Kelime("Kavşak",         "crossroads",      R.raw.v107_kavsak));
        kelimeler.add(new Kelime("Kaza",           "accident",        R.raw.v108_kaza));
        kelimeler.add(new Kelime("Kemer",          "belt",            R.raw.v109_kemer));
        kelimeler.add(new Kelime("Keşke",          "if only",         R.raw.v110_keske));
        kelimeler.add(new Kelime("Kim",            "who",             R.raw.v111_kim));
        kelimeler.add(new Kelime("Kimlik",         "identity",        R.raw.v112_kimlik));
        kelimeler.add(new Kelime("Kira",           "rent",            R.raw.v113_kira));
        kelimeler.add(new Kelime("Kitap",          "book",            R.raw.v114_kitap));
        kelimeler.add(new Kelime("Kıyma",          "mince",           R.raw.v115_kiyma));
        kelimeler.add(new Kelime("Kız",            "female",          R.raw.v116_kiz));
        kelimeler.add(new Kelime("Koku",           "smell",           R.raw.v117_koku));
        kelimeler.add(new Kelime("Kolonya",        "cologne",         R.raw.v118_kolonya));
        kelimeler.add(new Kelime("Kömür",          "coal",            R.raw.v119_komur));
        kelimeler.add(new Kelime("Köpek",          "dog",             R.raw.v120_kopek));
        kelimeler.add(new Kelime("Köprü",          "bridge",          R.raw.v121_kopru));
        kelimeler.add(new Kelime("Kötü",           "bad",             R.raw.v122_kotu));
        kelimeler.add(new Kelime("Kucak",          "lap",             R.raw.v123_kucak));
        kelimeler.add(new Kelime("Leke",           "stain",           R.raw.v124_leke));
        kelimeler.add(new Kelime("Maaş",           "salary",          R.raw.v125_maas));
        kelimeler.add(new Kelime("Makas",          "scissors",        R.raw.v126_makas));
        kelimeler.add(new Kelime("Masa",           "table",           R.raw.v127_masa));
        kelimeler.add(new Kelime("Maşallah",       "god preserve",    R.raw.v128_masallah));
        kelimeler.add(new Kelime("Melek",          "angel",           R.raw.v129_melek));
        kelimeler.add(new Kelime("Memnun Olmak",   "be pleased",      R.raw.v130_memnun_olmak));
        kelimeler.add(new Kelime("Mendil",         "napkin",          R.raw.v131_mendil));
        kelimeler.add(new Kelime("Merdiven",       "stairs",          R.raw.v132_merdiven));
        kelimeler.add(new Kelime("Misafir",        "guest",           R.raw.v133_misafir));
        kelimeler.add(new Kelime("Müdür",          "manager",         R.raw.v134_mudur));
        kelimeler.add(new Kelime("Musluk",         "tap",             R.raw.v135_musluk));
        kelimeler.add(new Kelime("Nasıl",          "how",             R.raw.v136_nasil));
        kelimeler.add(new Kelime("Neden",          "why",             R.raw.v137_neden));
        kelimeler.add(new Kelime("Nerede",         "where",           R.raw.v138_nerede));
        kelimeler.add(new Kelime("Nine",           "grandmother",     R.raw.v139_nine));
        kelimeler.add(new Kelime("Ocak",           "oven",            R.raw.v140_ocak));
        kelimeler.add(new Kelime("Oda",            "room",            R.raw.v141_oda));
        kelimeler.add(new Kelime("Odun",           "wood",            R.raw.v142_odun));
        kelimeler.add(new Kelime("Öğretmen",       "teacher",         R.raw.v143_ogretmen));
        kelimeler.add(new Kelime("Okul",           "school",          R.raw.v144_okul));
        kelimeler.add(new Kelime("Olimpiyat",      "olympiad",        R.raw.v145_olimpiyat));
        kelimeler.add(new Kelime("Olmaz",          "nope",            R.raw.v146_olmaz));
        kelimeler.add(new Kelime("Olur",           "alright",         R.raw.v147_olur));
        kelimeler.add(new Kelime("Onlar",          "they",            R.raw.v148_onlar));
        kelimeler.add(new Kelime("Orman",          "forest",          R.raw.v149_orman));
        kelimeler.add(new Kelime("Oruç",           "fasting",         R.raw.v150_oruc));
        kelimeler.add(new Kelime("Özür Dilemek",   "apologize",       R.raw.v151_ozur_dilemek));
        kelimeler.add(new Kelime("Pamuk",          "cotton",          R.raw.v152_pamuk));
        kelimeler.add(new Kelime("Pantolon",       "trousers",        R.raw.v153_pantolon));
        kelimeler.add(new Kelime("Para",           "money",           R.raw.v154_para));
        kelimeler.add(new Kelime("Pastırma",       "pastrami",        R.raw.v155_pastirma));
        kelimeler.add(new Kelime("Patates",        "potato",          R.raw.v156_patates));
        kelimeler.add(new Kelime("Pazar",          "sunday",          R.raw.v157_pazar));
        kelimeler.add(new Kelime("Pazartesi",      "monday",          R.raw.v158_pazartesi));
        kelimeler.add(new Kelime("Pencere",        "window",          R.raw.v159_pencere));
        kelimeler.add(new Kelime("Perşembe",       "thursday",        R.raw.v160_persembe));
        kelimeler.add(new Kelime("Piknik",         "picnic",          R.raw.v161_piknik));
        kelimeler.add(new Kelime("Polis",          "police",          R.raw.v162_polis));
        kelimeler.add(new Kelime("Psikoloji",      "psychology",      R.raw.v163_psikoloji));
        kelimeler.add(new Kelime("Rica Etmek",     "request",         R.raw.v164_rica_etmek));
        kelimeler.add(new Kelime("Saat",           "hour",            R.raw.v165_saat));
        kelimeler.add(new Kelime("Sabun",          "soap",            R.raw.v166_sabun));
        kelimeler.add(new Kelime("Salça",          "sauce",           R.raw.v167_salca));
        kelimeler.add(new Kelime("Salı",           "tuesday",         R.raw.v168_sali));
        kelimeler.add(new Kelime("Şampiyon",       "champion",        R.raw.v169_sampiyon));
        kelimeler.add(new Kelime("Şapka",          "hat",             R.raw.v170_sapka));
        kelimeler.add(new Kelime("Savaş",          "war",             R.raw.v171_savas));
        kelimeler.add(new Kelime("Şeker",          "sugar",           R.raw.v172_seker));
        kelimeler.add(new Kelime("Selam",          "hi",              R.raw.v173_selam));
        kelimeler.add(new Kelime("Şemsiye",        "umbrella",        R.raw.v174_semsiye));
        kelimeler.add(new Kelime("Sen",            "you",             R.raw.v175_sen));
        kelimeler.add(new Kelime("Senet",          "bill",            R.raw.v176_senet));
        kelimeler.add(new Kelime("Serbest",        "free",            R.raw.v177_serbest));
        kelimeler.add(new Kelime("Ses",            "voice",           R.raw.v178_ses));
        kelimeler.add(new Kelime("Sevmek",         "love",            R.raw.v179_sevmek));
        kelimeler.add(new Kelime("Şeytan",         "evil",            R.raw.v180_seytan));
        kelimeler.add(new Kelime("Sınır",          "border",          R.raw.v181_sinir));
        kelimeler.add(new Kelime("Siz",            "you (plural)",    R.raw.v182_siz));
        kelimeler.add(new Kelime("Söylemek",       "say",             R.raw.v183_soylemek));
        kelimeler.add(new Kelime("Söz",            "promise",         R.raw.v184_soz));
        kelimeler.add(new Kelime("Süt",            "milk",            R.raw.v185_sut));
        kelimeler.add(new Kelime("Tamam",          "okay",            R.raw.v186_tamam));
        kelimeler.add(new Kelime("Tarak",          "comb",            R.raw.v187_tarak));
        kelimeler.add(new Kelime("Tarih",          "date",            R.raw.v188_tarih));
        kelimeler.add(new Kelime("Tatil",          "holiday",         R.raw.v189_tatil));
        kelimeler.add(new Kelime("Tatlı",          "sweet",           R.raw.v190_tatli));
        kelimeler.add(new Kelime("Tavan",          "ceiling",         R.raw.v191_tavan));
        kelimeler.add(new Kelime("Tehlike",        "danger",          R.raw.v192_tehlike));
        kelimeler.add(new Kelime("Telefon",        "telephone",       R.raw.v193_telefon));
        kelimeler.add(new Kelime("Terazi",         "scales",          R.raw.v194_terazi));
        kelimeler.add(new Kelime("Terzi",          "tailor",          R.raw.v195_terzi));
        kelimeler.add(new Kelime("Teşekkür",       "thanks",          R.raw.v196_tesekkur));
        kelimeler.add(new Kelime("Tornavida",      "screwdriver",     R.raw.v197_tornavida));
        kelimeler.add(new Kelime("Türkiye",        "turkey",          R.raw.v198_turkiye));
        kelimeler.add(new Kelime("Turuncu",        "orange",          R.raw.v199_turuncu));
        kelimeler.add(new Kelime("Tuvalet",        "toilet",          R.raw.v200_tuvalet));
        kelimeler.add(new Kelime("Un",             "flour",           R.raw.v201_un));
        kelimeler.add(new Kelime("Uzak",           "far",             R.raw.v202_uzak));
        kelimeler.add(new Kelime("Üzgün",          "sad",             R.raw.v203_uzgun));
        kelimeler.add(new Kelime("Var",            "existing",        R.raw.v204_var));
        kelimeler.add(new Kelime("Vergi",          "tax",             R.raw.v205_vergi));
        kelimeler.add(new Kelime("Yakın",          "near",            R.raw.v206_yakin));
        kelimeler.add(new Kelime("Yalnız",         "alone",           R.raw.v207_yalniz));
        kelimeler.add(new Kelime("Yanlış",         "wrong",           R.raw.v208_yanlis));
        kelimeler.add(new Kelime("Yapmak",         "do",              R.raw.v209_yapmak));
        kelimeler.add(new Kelime("Yarabandı",      "band-aid",        R.raw.v210_yarabandi));
        kelimeler.add(new Kelime("Yardım",         "help",            R.raw.v211_yardim));
        kelimeler.add(new Kelime("Yarın",          "tomorrow",        R.raw.v212_yarin));
        kelimeler.add(new Kelime("Yasak",          "forbidden",       R.raw.v213_yasak));
        kelimeler.add(new Kelime("Yastık",         "pillow",          R.raw.v214_yastik));
        kelimeler.add(new Kelime("Yatak",          "bed",             R.raw.v215_yatak));
        kelimeler.add(new Kelime("Yavaş",          "slow",            R.raw.v216_yavas));
        kelimeler.add(new Kelime("Yemek",          "eat",             R.raw.v217_yemek));
        kelimeler.add(new Kelime("Yemek Pişirmek", "cook",            R.raw.v218_yemek_pisirmek));
        kelimeler.add(new Kelime("Yıldız",         "star",            R.raw.v219_yildiz));
        kelimeler.add(new Kelime("Yok",            "absent",          R.raw.v220_yok));
        kelimeler.add(new Kelime("Yol",            "road",            R.raw.v221_yol));
        kelimeler.add(new Kelime("Yorgun",         "tired",           R.raw.v222_yorgun));
        kelimeler.add(new Kelime("Yumurta",        "egg",             R.raw.v223_yumurta));
        kelimeler.add(new Kelime("Zaman",          "time",            R.raw.v224_zaman));
        kelimeler.add(new Kelime("Zor",            "difficult",       R.raw.v225_zor));

        kelimeler.sort((a, b) -> a.getGorunenAd().compareToIgnoreCase(b.getGorunenAd()));

        kelimeListesiView.setLayoutManager(new LinearLayoutManager(this));
        kelimeListesiView.setItemAnimator(null);

        adapter = new KelimeAdapter(kelimeler, secilen ->
                videoPaneliniAcVeOynat(secilen.getGorunenAd(), secilen.getVideoId())
        );
        kelimeListesiView.setAdapter(adapter);

        aramaCubugu.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (adapter != null) adapter.filtrele(s.toString());
            }
        });

        btnGeri.setOnClickListener(v -> {
            if (videoPanelContainer.getVisibility() == View.VISIBLE) videoPaneliniKapat();
            else finish();
        });

        findViewById(R.id.btnKapat).setOnClickListener(v -> videoPaneliniKapat());

        btnSpeed05.setOnClickListener(v -> hizSec(btnSpeed05, 0.5f));
        btnSpeed10.setOnClickListener(v -> hizSec(btnSpeed10, 1.0f));
        btnSpeed15.setOnClickListener(v -> hizSec(btnSpeed15, 1.5f));
    }

    private void videoPaneliniAcVeOynat(String ad, int resId) {
        secilenKelimeBaslik.setText(ad);
        videoPanelContainer.setVisibility(View.VISIBLE);

        String path = "android.resource://" + getPackageName() + "/" + resId;
        player.setMediaItem(MediaItem.fromUri(Uri.parse(path)));
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.prepare();
        player.play();

        hizSec(btnSpeed10, 1.0f);
    }

    private void videoPaneliniKapat() {
        videoPanelContainer.setVisibility(View.GONE);
        if (player.isPlaying()) player.stop();
    }

    private void hizSec(Button secilen, float hiz) {
        if (aktifHizButonu != null) {
            aktifHizButonu.setBackgroundResource(R.drawable.bg_speed_button);
            aktifHizButonu.setTextColor(0xFF007AFF);
        }
        secilen.setBackgroundResource(R.drawable.bg_speed_button_active);
        secilen.setTextColor(0xFFFFFFFF);
        aktifHizButonu = secilen;
        player.setPlaybackParameters(new PlaybackParameters(hiz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) player.release();
    }
}