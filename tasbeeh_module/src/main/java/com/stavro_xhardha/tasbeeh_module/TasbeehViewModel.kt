package com.stavro_xhardha.tasbeeh_module

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasbeehViewModel : ViewModel() {

    val tasbeehList: MutableLiveData<List<Tasbeeh>> = MutableLiveData()

    init {
        initList()
    }

    fun initList() {
        tasbeehList.value = listOf(
            Tasbeeh("سُبْحاَنَ اللهِ", "Subhanallah", "Glory be to Allah"),
            Tasbeeh("اَلْحَمْدُ لِلهِِ", "Alhamdulillah", "Praise be to Allah"),
            Tasbeeh("اَلّلَهُ اَكْبَرْ", "Allahu Akbar", "Allah is the Greatest"),
            Tasbeeh(
                "لاَأِلَاهَ اِلاَّ اللّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلىَ كُلِّ شَيْءِِقَدِيرُ",
                "La-ilaha-ill’Allahu wahdahu-la-sharikalah, Lahul-mulku wa lahul-hamdu wa huwa ala-kulli-shay’in-Qadir," +
                        " Subhana rabbiye’laliyyi’l-a’le’l-vehhâb",
                "There is no god but Allah alone, He has no partners, to Him belongs dominion and to Him belongs praises, " +
                        "and He has power over all things"
            ),
            Tasbeeh("لَا إِلٰهَ إِلَّا ٱللهِ", "La Ilaha Illa Allah", "There is no god but Allah"),
            Tasbeeh(
                "سبحان الله وبحمده سبحان الله العظيم",
                "Subhanallahi wabihamdihi subhanallahil adhim",
                "Allah is free from imperfection and all praise is due to him"
            ),
            Tasbeeh("أَسْتَغْفِرُ اللّٰهَِ", "Astagfirullah", "I ask forgiveness from Allah"),
            Tasbeeh(
                "لَا إِلٰهَ إِلَّا ٱلله مُحَمَّدٌ رَسُولُ ٱلله",
                "La ilaha ila Allah, Muhamedun rasul Allah",
                "There is no god but God. Muhammad is the messenger of God."
            ),
            Tasbeeh(
                "اللهم أنت السلام ومنك السلام تباركت يا ذا الجلال والإكرامِ",
                "Allahumma antas salam wa minkas salam tabarakta (ya) dhal jalali wal ikram",
                "O Allah, You are peace, peace comes from You. Blessed are You O Possessor of Glory and Honour"
            )
        )
    }
}