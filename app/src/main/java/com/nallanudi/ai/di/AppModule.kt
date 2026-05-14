package com.nallanudi.ai.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nallanudi.ai.data.local.AppDatabase
import com.nallanudi.ai.data.local.TermDao
import com.nallanudi.ai.data.local.TermEntity
import com.nallanudi.ai.data.repository.TermRepositoryImpl
import com.nallanudi.ai.domain.repository.TermRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        termDaoProvider: Provider<TermDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nallanudi_db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    seedDatabase(termDaoProvider.get())
                }
            }
        }).build()
    }

    @Provides
    fun provideTermDao(database: AppDatabase): TermDao = database.termDao()

    @Provides
    @Singleton
    fun provideTermRepository(termDao: TermDao): TermRepository = TermRepositoryImpl(termDao)

    private suspend fun seedDatabase(dao: TermDao) {
        val initialTerms = listOf(
            TermEntity(
                englishWord = "Photosynthesis",
                kannadaMeaning = "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ (Dyutisonsleshene)",
                kannadaExplanation = "ಸಸ್ಯಗಳು ಸೂರ್ಯನ ಬೆಳಕನ್ನು ಬಳಸಿಕೊಂಡು ಆಹಾರವನ್ನು ತಯಾರಿಸುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process by which green plants and some other organisms use sunlight to synthesize foods with the help of chlorophyll.",
                example = "Photosynthesis is essential for life on Earth as it produces oxygen.",
                pronunciation = "/ˌfōtōˈsinTHəsəs/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Trigonometry",
                kannadaMeaning = "ತ್ರಿಕೋನಮಿತಿ (Trikonamiti)",
                kannadaExplanation = "ತ್ರಿಕೋನಗಳ ಬದಿಗಳು ಮತ್ತು ಕೋನಗಳ ನಡುವಿನ ಸಂಬಂಧಗಳನ್ನು ಅಧ್ಯಯನ ಮಾಡುವ ಗಣಿತದ ಒಂದು ಶಾಖೆ.",
                englishExplanation = "The branch of mathematics dealing with the relations of the sides and angles of triangles and with the relevant functions of any angles.",
                example = "Trigonometry is used in navigation and engineering.",
                pronunciation = "/ˌtriɡəˈnämətrē/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Algorithm",
                kannadaMeaning = "ಕ್ರಮಾವಳಿ (Kramavali)",
                kannadaExplanation = "ಒಂದು ಸಮಸ್ಯೆಯನ್ನು ಪರಿಹರಿಸಲು ಅಥವಾ ಒಂದು ಕಾರ್ಯವನ್ನು ನಿರ್ವಹಿಸಲು ಅನುಸರಿಸಬೇಕಾದ ಹಂತ-ಹಂತದ ವಿಧಾನ.",
                englishExplanation = "A process or set of rules to be followed in calculations or other problem-solving operations, especially by a computer.",
                example = "Social media feeds use complex algorithms to show relevant content.",
                pronunciation = "/ˈalɡəˌriTHəm/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Inflation",
                kannadaMeaning = "ಹಣದುಬ್ಬರ (Hanadubbara)",
                kannadaExplanation = "ಸರಕು ಮತ್ತು ಸೇವೆಗಳ ಬೆಲೆಗಳಲ್ಲಿನ ಸಾಮಾನ್ಯ ಏರಿಕೆ ಮತ್ತು ಹಣದ ಕೊಳ್ಳುವ ಶಕ್ತಿಯ ಇಳಿಕೆ.",
                englishExplanation = "A general increase in prices and fall in the purchasing value of money.",
                example = "High inflation rates can lead to economic instability.",
                pronunciation = "/inˈflāSH(ə)n/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Gravity",
                kannadaMeaning = "ಗುರುತ್ವಾಕರ್ಷಣೆ (Gurutvakarshane)",
                kannadaExplanation = "ಎರಡು ವಸ್ತುಗಳು ಪರಸ್ಪರ ಆಕರ್ಷಿಸುವ ಶಕ್ತಿ.",
                englishExplanation = "The force that attracts a body toward the center of the earth, or toward any other physical body having mass.",
                example = "Gravity keeps the planets in orbit around the sun.",
                pronunciation = "/ˈɡravədē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Encryption",
                kannadaMeaning = "ಗೂಢಲಿಪೀಕರಣ (Gudhalipikarana)",
                kannadaExplanation = "ಮಾಹಿತಿಯನ್ನು ಅನಧಿಕೃತ ವ್ಯಕ್ತಿಗಳು ಓದಲು ಸಾಧ್ಯವಾಗದಂತೆ ಸಂಕೇತ ರೂಪಕ್ಕೆ ಬದಲಾಯಿಸುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process of converting information or data into a code, especially to prevent unauthorized access.",
                example = "Encryption is vital for online banking security.",
                pronunciation = "/enˈkripSH(ə)n/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Liability",
                kannadaMeaning = "ಹೊಣೆಗಾರಿಕೆ (Honegarike)",
                kannadaExplanation = "ಒಬ್ಬ ವ್ಯಕ್ತಿ ಅಥವಾ ಕಂಪನಿಯು ಇತರರಿಗೆ ನೀಡಬೇಕಾದ ಹಣಕಾಸಿನ ಸಾಲ ಅಥವಾ ಬಾಧ್ಯತೆ.",
                englishExplanation = "A thing for which someone is responsible, especially a debt or financial obligation.",
                example = "Accounts payable are a common current liability for businesses.",
                pronunciation = "/ˌlīəˈbilədē/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Asymptote",
                kannadaMeaning = "ಅನಂತಸ್ಪರ್ಶಕ (Anantasparshaka)",
                kannadaExplanation = "ಒಂದು ವಕ್ರರೇಖೆಯು ಅನಂತದ ಕಡೆಗೆ ಚಲಿಸಿದಾಗ ಸಮೀಪಿಸುವ ಆದರೆ ಎಂದಿಗೂ ಸ್ಪರ್ಶಿಸದ ಸರಳರೇಖೆ.",
                englishExplanation = "A line that a curve approaches as it heads towards infinity.",
                example = "The function f(x) = 1/x has a horizontal asymptote at y = 0.",
                pronunciation = "/ˈasəmˌtōt/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Centrifugal Force",
                kannadaMeaning = "ಕೇಂದ್ರಾಪಗಾಮಿ ಬಲ (Kendrapagami Bala)",
                kannadaExplanation = "ವೃತ್ತಾಕಾರದ ಪಥದಲ್ಲಿ ಚಲಿಸುವ ವಸ್ತುವಿನ ಮೇಲೆ ಕೇಂದ್ರದಿಂದ ಹೊರಕ್ಕೆ ತಳ್ಳುವಂತೆ ಭಾಸವಾಗುವ ಬಲ.",
                englishExplanation = "An apparent force that acts outward on a body moving around a center.",
                example = "A centrifugal governor uses centrifugal force to control engine speed.",
                pronunciation = "/senˈtrifjəɡəl fôrs/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Mitosis",
                kannadaMeaning = "ಸಮವಿಭಜನೆ (Samavibhajane)",
                kannadaExplanation = "ಒಂದು ಜೀವಕೋಶವು ಎರಡು ಸಮಾನವಾದ ಪುತ್ರಿಕಾ ಜೀವಕೋಶಗಳಾಗಿ ವಿಭಜನೆಯಾಗುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "A type of cell division that results in two daughter cells each having the same number and kind of chromosomes as the parent nucleus.",
                example = "Mitosis is crucial for growth and tissue repair.",
                pronunciation = "/mīˈtōsəs/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Valency",
                kannadaMeaning = "ವ್ಯಾಲೆನ್ಸಿ (Valency)",
                kannadaExplanation = "ಒಂದು ಪರಮಾಣು ಇತರ ಪರಮಾಣುಗಳೊಂದಿಗೆ ಸಂಯೋಜನೆಗೊಳ್ಳುವ ಸಾಮರ್ಥ್ಯ.",
                englishExplanation = "The combining power of an element, especially as measured by the number of hydrogen atoms it can displace or combine with.",
                example = "Carbon has a valency of four, allowing it to form complex molecules.",
                pronunciation = "/ˈvālənsē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Quadratic Equation",
                kannadaMeaning = "ವರ್ಗ ಸಮೀಕರಣ (Varga Samikarana)",
                kannadaExplanation = "ಗರಿಷ್ಠ ಘಾತ ಎರಡು ಇರುವ ಗಣಿತದ ಸಮೀಕರಣ.",
                englishExplanation = "An equation where the highest exponent of the variable (usually x) is a square.",
                example = "x² + 5x + 6 = 0 is a quadratic equation.",
                pronunciation = "/kwəˈdradik əˈkwāZHən/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Calculus",
                kannadaMeaning = "ಕಲನಶಾಸ್ತ್ರ (Kalanashastra)",
                kannadaExplanation = "ಬದಲಾವಣೆಯ ದರಗಳು ಮತ್ತು ಸಣ್ಣ ಪ್ರಮಾಣಗಳ ಮೊತ್ತಗಳನ್ನು ಅಧ್ಯಯನ ಮಾಡುವ ಗಣಿತದ ಶಾಖೆ.",
                englishExplanation = "The mathematical study of continuous change, encompassing differential and integral calculus.",
                example = "Calculus is used in physics to model movement and change.",
                pronunciation = "/ˈkalkyələs/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Probability",
                kannadaMeaning = "ಸಂಭವನೀಯತೆ (Sambhavaniyate)",
                kannadaExplanation = "ಒಂದು ಘಟನೆ ಸಂಭವಿಸುವ ಸಾಧ್ಯತೆಯ ಅಳತೆ.",
                englishExplanation = "The extent to which something is likely to happen or be the case.",
                example = "The probability of flipping a coin and getting heads is 0.5.",
                pronunciation = "/ˌpräbəˈbilədē/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Dividend",
                kannadaMeaning = "ಲಾಭಾಂಶ (Labhansha)",
                kannadaExplanation = "ಕಂಪನಿಯ ಲಾಭದ ಒಂದು ಭಾಗವನ್ನು ಅದರ ಷೇರುದಾರರಿಗೆ ಹಂಚಿಕೆ ಮಾಡುವುದು.",
                englishExplanation = "A sum of money paid regularly by a company to its shareholders out of its profits.",
                example = "Investors often look for stocks that pay regular dividends.",
                pronunciation = "/ˈdivəˌdend/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Liquidity",
                kannadaMeaning = "ದ್ರವ್ಯತೆ (Dravyate)",
                kannadaExplanation = "ಒಂದು ಆಸ್ತಿಯನ್ನು ಅದರ ಮೌಲ್ಯಕ್ಕೆ ಧಕ್ಕೆ ಉಂಟಾಗದಂತೆ ಎಷ್ಟು ಬೇಗ ಹಣವಾಗಿ ಬದಲಾಯಿಸಬಹುದು ಎಂಬ ಸಾಮರ್ಥ್ಯ.",
                englishExplanation = "The availability of liquid assets to a market or company.",
                example = "Cash is the most liquid asset.",
                pronunciation = "/liˈkwidədē/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Audit",
                kannadaMeaning = "ಲೆಕ್ಕಪರಿಶೋಧನೆ (Lekkaparishodhane)",
                kannadaExplanation = "ಒಂದು ಸಂಸ್ಥೆಯ ಅಥವಾ ವ್ಯಕ್ತಿಯ ಹಣಕಾಸಿನ ದಾಖಲೆಗಳ ಅಧಿಕೃತ ಪರೀಕ್ಷೆ.",
                englishExplanation = "An official inspection of an individual's or organization's accounts, typically by an independent body.",
                example = "The company undergoes an annual audit to ensure financial transparency.",
                pronunciation = "/ˈôdit/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Compiler",
                kannadaMeaning = "ಕಂಪೈಲರ್ (Compiler)",
                kannadaExplanation = "ಪ್ರೋಗ್ರಾಮಿಂಗ್ ಭಾಷೆಯಲ್ಲಿ ಬರೆದ ಕೋಡ್ ಅನ್ನು ಕಂಪ್ಯೂಟರ್ ಓದಬಲ್ಲ ಯಂತ್ರ ಭಾಷೆಗೆ ಬದಲಾಯಿಸುವ ಪ್ರೋಗ್ರಾಂ.",
                englishExplanation = "A program that converts instructions into a machine-code or lower-level form so that they can be read and executed by a computer.",
                example = "The Kotlin compiler transforms source code into bytecode.",
                pronunciation = "/kəmˈpīlər/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Inheritance",
                kannadaMeaning = "ಪರಂಪರೆ/ಅನುವಂಶೀಯತೆ (Parampare/Anuvamshiyate)",
                kannadaExplanation = "ಒಂದು ಕ್ಲಾಸ್ ಮತ್ತೊಂದು ಕ್ಲಾಸ್‌ನ ಗುಣಲಕ್ಷಣಗಳನ್ನು ಮತ್ತು ವಿಧಾನಗಳನ್ನು ಪಡೆಯುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The mechanism by which one class acquires the properties and behaviors of another class.",
                example = "In Java, inheritance allows for code reusability.",
                pronunciation = "/inˈherədəns/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Variable",
                kannadaMeaning = "ಚರತ್ವ/ವೇರಿಯೇಬಲ್ (Charatva/Variable)",
                kannadaExplanation = "ಪ್ರೋಗ್ರಾಮಿಂಗ್‌ನಲ್ಲಿ ಮಾಹಿತಿಯನ್ನು ಶೇಖರಿಸಿಡಲು ಬಳಸುವ ಒಂದು ಸ್ಥಳದ ಹೆಸರು.",
                englishExplanation = "A storage location paired with an associated symbolic name, which contains some known or unknown quantity of information referred to as a value.",
                example = "Declare a variable 'x' to store an integer value.",
                pronunciation = "/ˈverēəbəl/",
                subject = "Computer Science"
            ),
            // --- Mathematics ---
            TermEntity(
                englishWord = "Derivative",
                kannadaMeaning = "ವ್ಯುತ್ಪನ್ನ (Vyutpanna)",
                kannadaExplanation = "ಗಣಿತದಲ್ಲಿ, ಒಂದು ಪ್ರಮಾಣವು ಮತ್ತೊಂದು ಪ್ರಮಾಣಕ್ಕೆ ಹೋಲಿಸಿದರೆ ಹೇಗೆ ಬದಲಾಗುತ್ತದೆ ಎಂಬ ದರ.",
                englishExplanation = "The rate of change of a function with respect to a variable.",
                example = "The derivative of x² is 2x.",
                pronunciation = "/dəˈrivədiv/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Integral",
                kannadaMeaning = "ಸಂಕಲನ (Sankalana)",
                kannadaExplanation = "ವಕ್ರರೇಖೆಯ ಅಡಿಯ ಕೆಳಗಿನ ವಿಸ್ತೀರ್ಣವನ್ನು ಕಂಡುಹಿಡಿಯಲು ಬಳಸುವ ಗಣಿತದ ವಿಧಾನ.",
                englishExplanation = "A function of which a given function is the derivative, used to find areas under curves.",
                example = "Integration is the reverse process of differentiation.",
                pronunciation = "/ˈin-tə-ɡrəl/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Hypotenuse",
                kannadaMeaning = "ವಿಕರ್ಣ (Vikarna)",
                kannadaExplanation = "ಲಂಬಕೋನ ತ್ರಿಕೋನದಲ್ಲಿ ಅತಿ ಉದ್ದವಾದ ಬದಿ.",
                englishExplanation = "The longest side of a right-angled triangle, opposite the right angle.",
                example = "In a 3-4-5 triangle, 5 is the hypotenuse.",
                pronunciation = "/hīˈpätəˌn(y)o͞os/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Isosceles",
                kannadaMeaning = "ಸಮದ್ವಿಬಾಹು (Samadvibahu)",
                kannadaExplanation = "ಎರಡು ಬದಿಗಳು ಸಮನಾಗಿರುವ ತ್ರಿಕೋನ.",
                englishExplanation = "A triangle having two sides of equal length.",
                example = "An isosceles triangle has two equal angles.",
                pronunciation = "/īˈsäsəˌlēz/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Polynomial",
                kannadaMeaning = "ಬಹುಪದೋಕ್ತಿ (Bahupadokti)",
                kannadaExplanation = "ಅಸ್ಥಿರಗಳು ಮತ್ತು ಗುಣಾಂಕಗಳನ್ನು ಒಳಗೊಂಡಿರುವ ಗಣಿತದ ಅಭಿವ್ಯಕ್ತಿ.",
                englishExplanation = "An expression consisting of variables and coefficients, that involves only the operations of addition, subtraction, multiplication, and non-negative integer exponentiation.",
                example = "x² + 3x + 2 is a polynomial.",
                pronunciation = "/ˌpäləˈnōmēəl/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Mean",
                kannadaMeaning = "ಸರಾಸರಿ (Sarasari)",
                kannadaExplanation = "ಸಂಖ್ಯೆಗಳ ಗುಂಪಿನ ಮೊತ್ತವನ್ನು ಆ ಗುಂಪಿನ ಸಂಖ್ಯೆಗಳ ಒಟ್ಟು ಎಣಿಕೆಯಿಂದ ಭಾಗಿಸಿದಾಗ ಬರುವ ಫಲಿತಾಂಶ.",
                englishExplanation = "The average of a set of numbers.",
                example = "The mean of 2, 4, and 6 is 4.",
                pronunciation = "/mēn/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Median",
                kannadaMeaning = "ಮಧ್ಯಂಕ (Madhyanka)",
                kannadaExplanation = "ಏರಿಕೆ ಅಥವಾ ಇಳಿಕೆ ಕ್ರಮದಲ್ಲಿ ಜೋಡಿಸಿದ ಸಂಖ್ಯೆಗಳ ಗುಂಪಿನಲ್ಲಿ ಸರಿಯಾಗಿ ಮಧ್ಯದಲ್ಲಿರುವ ಸಂಖ್ಯೆ.",
                englishExplanation = "The middle value in a list of numbers ordered from least to greatest.",
                example = "In the set {1, 3, 5}, the median is 3.",
                pronunciation = "/ˈmēdēən/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Vector",
                kannadaMeaning = "ದಿಶಾಪರಿಮಾಣ (Dishaparimana)",
                kannadaExplanation = "ಗಾತ್ರ ಮತ್ತು ದಿಕ್ಕು ಎರಡನ್ನೂ ಹೊಂದಿರುವ ಪ್ರಮಾಣ.",
                englishExplanation = "A quantity having direction as well as magnitude.",
                example = "Velocity and acceleration are vectors.",
                pronunciation = "/ˈvektər/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Logarithm",
                kannadaMeaning = "ಲಘುಗಣಕ (Laghuganaka)",
                kannadaExplanation = "ಒಂದು ಸಂಖ್ಯೆಯನ್ನು ಪಡೆಯಲು ಮತ್ತೊಂದು ನಿಗದಿತ ಸಂಖ್ಯೆಯನ್ನು ಎಷ್ಟನೇ ಘಾತಕ್ಕೆ ಏರಿಸಬೇಕು ಎಂದು ತಿಳಿಸುವ ಸಂಖ್ಯೆ.",
                englishExplanation = "The exponent to which a base must be raised to produce a given number.",
                example = "The logarithm of 100 to base 10 is 2.",
                pronunciation = "/ˈlôɡəˌriTHəm/",
                subject = "Mathematics"
            ),
            // --- Science ---
            TermEntity(
                englishWord = "Catalyst",
                kannadaMeaning = "ಉತ್ಪ್ರೇರಕ (Utpreraka)",
                kannadaExplanation = "ತಾನೇ ಬದಲಾಗದೆ ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಯ ವೇಗವನ್ನು ಹೆಚ್ಚಿಸುವ ಪದಾರ್ಥ.",
                englishExplanation = "A substance that increases the rate of a chemical reaction without itself undergoing any permanent chemical change.",
                example = "Enzymes are biological catalysts.",
                pronunciation = "/ˈkad(ə)ləst/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Sublimation",
                kannadaMeaning = "ಉತ್ಪತನ (Utpatana)",
                kannadaExplanation = "ಒಂದು ಘನ ಪದಾರ್ಥವು ದ್ರವವಾಗದೆ ನೇರವಾಗಿ ಅನಿಲವಾಗಿ ಬದಲಾಗುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process of a solid turning directly into a gas without passing through the liquid phase.",
                example = "Dry ice undergoes sublimation at room temperature.",
                pronunciation = "/ˌsəbləˈmāSH(ə)n/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Osmosis",
                kannadaMeaning = "ಅಭಿಸರಣೆ (Abhisarane)",
                kannadaExplanation = "ದ್ರಾವಣವು ಕಡಿಮೆ ಸಾಂದ್ರತೆಯಿಂದ ಹೆಚ್ಚು ಸಾಂದ್ರತೆಯ ಕಡೆಗೆ ಪೊರೆಯ ಮೂಲಕ ಹರಿಯುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The movement of solvent molecules through a semipermeable membrane into a region of higher solute concentration.",
                example = "Plants absorb water through their roots via osmosis.",
                pronunciation = "/äzˈmōsəs/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Friction",
                kannadaMeaning = "ಘರ್ಷಣೆ (Gharshane)",
                kannadaExplanation = "ಚಲಿಸುತ್ತಿರುವ ವಸ್ತು ಮತ್ತು ಮೇಲ್ಮೈ ನಡುವಿನ ಚಲನೆಯನ್ನು ವಿರೋಧಿಸುವ ಬಲ.",
                englishExplanation = "The resistance that one surface or object encounters when moving over another.",
                example = "Friction generates heat when you rub your hands together.",
                pronunciation = "/ˈfrikSH(ə)n/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Inertia",
                kannadaMeaning = "ಜಡತ್ವ (Jadatva)",
                kannadaExplanation = "ವಸ್ತುವಿನ ತನ್ನ ಚಲನೆ ಅಥವಾ ಸ್ಥಿತಿಯಲ್ಲಿನ ಬದಲಾವಣೆಯನ್ನು ವಿರೋಧಿಸುವ ಗುಣ.",
                englishExplanation = "The tendency of an object to resist changes in its state of motion.",
                example = "Due to inertia, you lean forward when a car suddenly stops.",
                pronunciation = "/iˈnərSHə/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Velocity",
                kannadaMeaning = "ವೇಗ (Vega/Velocity)",
                kannadaExplanation = "ಒಂದು ನಿರ್ದಿಷ್ಟ ದಿಕ್ಕಿನಲ್ಲಿ ವಸ್ತುವಿನ ಚಲನೆಯ ದರ.",
                englishExplanation = "The speed of something in a given direction.",
                example = "The car had a velocity of 60 km/h towards the north.",
                pronunciation = "/vəˈläsədē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Refraction",
                kannadaMeaning = "ವಕ್ರೀಭವನ (Vakribhavana)",
                kannadaExplanation = "ಬೆಳಕು ಒಂದು ಮಾಧ್ಯಮದಿಂದ ಮತ್ತೊಂದು ಮಾಧ್ಯಮಕ್ಕೆ ಹೋಗುವಾಗ ಬಾಗುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The bending of light as it passes from one medium to another.",
                example = "A straw looks bent in a glass of water due to refraction.",
                pronunciation = "/rəˈfrakSH(ə)n/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Kinetic Energy",
                kannadaMeaning = "ಚಲನಶಕ್ತಿ (Chalanashakti)",
                kannadaExplanation = "ವಸ್ತುವಿನ ಚಲನೆಯಿಂದ ಉಂಟಾಗುವ ಶಕ್ತಿ.",
                englishExplanation = "Energy that a body possesses by virtue of being in motion.",
                example = "A moving ball possesses kinetic energy.",
                pronunciation = "/kəˈnedik ˈenərjē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Evaporation",
                kannadaMeaning = "ಬಾಷ್ಪೀಕರಣ (Bashpikarna)",
                kannadaExplanation = "ದ್ರವವು ಅನಿಲವಾಗಿ ಬದಲಾಗುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process of a liquid turning into a gas.",
                example = "The sun causes evaporation of water from lakes.",
                pronunciation = "/əˌvapəˈrāSH(ə)n/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Electromagnetism",
                kannadaMeaning = "ವಿದ್ಯುತ್ಕಾಂತೀಯತೆ (Vidyutkantiyate)",
                kannadaExplanation = "ವಿದ್ಯುತ್ ಪ್ರವಾಹ ಮತ್ತು ಕಾಂತೀಯ ಕ್ಷೇತ್ರಗಳ ನಡುವಿನ ಪರಸ್ಪರ ಕ್ರಿಯೆಯ ಅಧ್ಯಯನ.",
                englishExplanation = "The interaction of electric currents or fields and magnetic fields.",
                example = "Electromagnetism is the principle behind electric motors.",
                pronunciation = "/əˌlektrōˈmaɡnəˌtizəm/",
                subject = "Science"
            ),
            // --- Commerce ---
            TermEntity(
                englishWord = "Asset",
                kannadaMeaning = "ಆಸ್ತಿ (Aasti)",
                kannadaExplanation = "ಒಬ್ಬ ವ್ಯಕ್ತಿ ಅಥವಾ ಕಂಪನಿಯು ಹೊಂದಿರುವ ಆರ್ಥಿಕ ಮೌಲ್ಯದ ವಸ್ತು.",
                englishExplanation = "A useful or valuable thing, person, or quality belonging to a person or company.",
                example = "Machinery and equipment are examples of fixed assets.",
                pronunciation = "/ˈaset/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Equity",
                kannadaMeaning = "ಷೇರು ಬಂಡವಾಳ (Sheru Bandavala)",
                kannadaExplanation = "ಕಂಪನಿಯ ಮಾಲೀಕತ್ವದ ಮೌಲ್ಯ.",
                englishExplanation = "The value of the shares issued by a company.",
                example = "The CEO owns 20% of the company's equity.",
                pronunciation = "/ˈekwədē/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Mortgage",
                kannadaMeaning = "ಅಡಮಾನ (Adamana)",
                kannadaExplanation = "ಆಸ್ತಿಯನ್ನು ಸಾಲಕ್ಕೆ ಭದ್ರತೆಯಾಗಿ ಇಡುವುದು.",
                englishExplanation = "A legal agreement by which a bank lends money at interest in exchange for taking title of the debtor's property.",
                example = "They took out a 30-year mortgage to buy their house.",
                pronunciation = "/ˈmôrɡij/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Ledger",
                kannadaMeaning = "ಖಾತೆ ಪುಸ್ತಕ (Khate Pustaka)",
                kannadaExplanation = "ಎಲ್ಲಾ ಹಣಕಾಸಿನ ವಹಿವಾಟುಗಳನ್ನು ದಾಖಲಿಸುವ ಮುಖ್ಯ ಪುಸ್ತಕ.",
                englishExplanation = "A book or other collection of financial accounts of a particular type.",
                example = "The accountant carefully updated the general ledger.",
                pronunciation = "/ˈlejər/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Inventory",
                kannadaMeaning = "ದಾಸ್ತಾನು (Dastānu)",
                kannadaExplanation = "ಮಾರಾಟಕ್ಕೆ ಅಥವಾ ಉತ್ಪಾದನೆಗೆ ಲಭ್ಯವಿರುವ ಸರಕುಗಳ ಪಟ್ಟಿ.",
                englishExplanation = "A complete list of items such as property, goods in stock, or the contents of a building.",
                example = "Retailers need to manage their inventory levels carefully.",
                pronunciation = "/ˈinvənˌtôrē/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Revenue",
                kannadaMeaning = "ಕಂದಾಯ/ಆದಾಯ (Kandaya/Adaya)",
                kannadaExplanation = "ಸರಕು ಅಥವಾ ಸೇವೆಗಳ ಮಾರಾಟದಿಂದ ಬರುವ ಒಟ್ಟು ಹಣ.",
                englishExplanation = "Income, especially when of a company or organization and of a substantial nature.",
                example = "The tech giant saw a 10% increase in annual revenue.",
                pronunciation = "/ˈrevəˌn(y)o͞o/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Profit",
                kannadaMeaning = "ಲಾಭ (Labha)",
                kannadaExplanation = "ಖರ್ಚುಗಳಿಗಿಂತ ಆದಾಯ ಹೆಚ್ಚಾದಾಗ ಸಿಗುವ ಹಣ.",
                englishExplanation = "A financial gain, especially the difference between the amount earned and the amount spent in buying, operating, or producing something.",
                example = "The shop made a decent profit this month.",
                pronunciation = "/ˈpräfət/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Market",
                kannadaMeaning = "ಮಾರುಕಟ್ಟೆ (Marukatte)",
                kannadaExplanation = "ಖರೀದಿದಾರರು ಮತ್ತು ಮಾರಾಟಗಾರರು ಸೇರುವ ಸ್ಥಳ ಅಥವಾ ವ್ಯವಸ್ಥೆ.",
                englishExplanation = "A regular gathering of people for the purchase and sale of provisions, livestock, and other commodities.",
                example = "The stock market crashed on Monday.",
                pronunciation = "/ˈmärkət/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Tax",
                kannadaMeaning = "ತೆರಿಗೆ (Terige)",
                kannadaExplanation = "ಸರ್ಕಾರವು ಸಾರ್ವಜನಿಕ ಸೇವೆಗಳಿಗಾಗಿ ವ್ಯಕ್ತಿಗಳು ಅಥವಾ ಕಂಪನಿಗಳ ಮೇಲೆ ವಿಧಿಸುವ ಕಡ್ಡಾಯ ಶುಲ್ಕ.",
                englishExplanation = "A compulsory contribution to state revenue, levied by the government on workers' income and business profits.",
                example = "The income tax rate was reduced last year.",
                pronunciation = "/taks/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Investment",
                kannadaMeaning = "ಹೂಡಿಕೆ (Hudike)",
                kannadaExplanation = "ಲಾಭ ಗಳಿಸುವ ಉದ್ದೇಶದಿಂದ ಹಣ ಅಥವಾ ಸಂಪನ್ಮೂಲಗಳನ್ನು ಬಳಸುವುದು.",
                englishExplanation = "The action or process of investing money for profit or material result.",
                example = "Real estate is considered a safe long-term investment.",
                pronunciation = "/inˈves(t)mənt/",
                subject = "Commerce"
            ),
            // --- Computer Science ---
            TermEntity(
                englishWord = "Database",
                kannadaMeaning = "ದತ್ತಾಂಶಸಂಗ್ರಹ (Dattanshasangraha)",
                kannadaExplanation = "ಮಾಹಿತಿಯನ್ನು ವ್ಯವಸ್ಥಿತವಾಗಿ ಶೇಖರಿಸಿಡುವ ವ್ಯವಸ್ಥೆ.",
                englishExplanation = "A structured set of data held in a computer, especially one that is accessible in various ways.",
                example = "The application stores user credentials in a secure database.",
                pronunciation = "/ˈdādəˌbās/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Frontend",
                kannadaMeaning = "ಮುಂಭಾಗ (Mumbhaga)",
                kannadaExplanation = "ಬಳಕೆದಾರರು ನೇರವಾಗಿ ನೋಡುವ ಮತ್ತು ಬಳಸುವ ವೆಬ್‌ಸೈಟ್ ಅಥವಾ ಆಪ್‌ನ ಭಾಗ.",
                englishExplanation = "The part of a website or software application that users interact with directly.",
                example = "He is a skilled frontend developer using React.",
                pronunciation = "/ˈfrəntˌend/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Backend",
                kannadaMeaning = "ಹಿಂಭಾಗ (Himbhaga)",
                kannadaExplanation = "ಸರ್ವರ್ ಬದಿಯಲ್ಲಿ ನಡೆಯುವ ಮತ್ತು ಬಳಕೆದಾರರಿಗೆ ಕಾಣಿಸದ ಪ್ರಕ್ರಿಯೆಗಳ ಭಾಗ.",
                englishExplanation = "The part of a computer system or application that is not directly accessed by the user, typically responsible for tasks like data storage and processing.",
                example = "Backend logic handles payment processing securely.",
                pronunciation = "/ˈbakˌend/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Debugging",
                kannadaMeaning = "ದೋಷನಿವಾರಣೆ (Doshanivarane)",
                kannadaExplanation = "ಪ್ರೋಗ್ರಾಂನಲ್ಲಿರುವ ತಪ್ಪುಗಳನ್ನು ಪತ್ತೆಹಚ್ಚಿ ಸರಿಪಡಿಸುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process of identifying and removing errors from computer hardware or software.",
                example = "I spent three hours debugging that logical error.",
                pronunciation = "/dēˈbəɡiNG/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Framework",
                kannadaMeaning = "ಫ್ರೇಮ್‌ವರ್ಕ್ (Framework)",
                kannadaExplanation = "ಆಪ್ ಅಭಿವೃದ್ಧಿಗಾಗಿ ಬಳಸುವ ಸಿದ್ಧಪಡಿಸಿದ ಪರಿಕರಗಳು ಮತ್ತು ನಿಯಮಗಳ ಗುಂಪು.",
                englishExplanation = "A basic structure underlying a system, concept, or text.",
                example = "Flutter is a popular framework for mobile app development.",
                pronunciation = "/ˈfrāmˌwərk/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Boolean",
                kannadaMeaning = "ಬೂಲಿಯನ್ (Boolean)",
                kannadaExplanation = "ಕೇವಲ ಎರಡು ಮೌಲ್ಯಗಳನ್ನು (ಸತ್ಯ ಅಥವಾ ಸುಳ್ಳು) ಹೊಂದಿರುವ ಡೇಟಾ ಪ್ರಕಾರ.",
                englishExplanation = "A data type that has one of two possible values which is intended to represent the two truth values of logic and Boolean algebra.",
                example = "The 'isLoggedIn' variable is a boolean.",
                pronunciation = "/ˈbo͞olēən/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Recursion",
                kannadaMeaning = "ಪುನರಾವರ್ತನೆ (Punaravartane)",
                kannadaExplanation = "ಒಂದು ಫಂಕ್ಷನ್ ತನ್ನನ್ನೇ ತಾನು ಕರೆದುಕೊಳ್ಳುವ ಪ್ರಕ್ರಿಯೆ.",
                englishExplanation = "The process of a function calling itself as a subroutine.",
                example = "Recursion can be used to solve the Fibonacci sequence efficiently.",
                pronunciation = "/rəˈkərZHən/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Syntax",
                kannadaMeaning = "ವಾಕ್ಯರಚನೆ/ಸಿಂಟ್ಯಾಕ್ಸ್ (Sintax)",
                kannadaExplanation = "ಪ್ರೋಗ್ರಾಮಿಂಗ್ ಭಾಷೆಯಲ್ಲಿ ಕೋಡ್ ಬರೆಯಲು ಅನುಸರಿಸಬೇಕಾದ ನಿಯಮಗಳು.",
                englishExplanation = "The set of rules that defines the combinations of symbols that are considered to be correctly structured programs in that language.",
                example = "Missing a semicolon causes a syntax error in C++.",
                pronunciation = "/ˈsinˌtaks/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Array",
                kannadaMeaning = "ಅರೇ/ಶ್ರೇಣಿ (Array)",
                kannadaExplanation = "ಒಂದೇ ಪ್ರಕಾರದ ಮಾಹಿತಿಯ ಗುಂಪನ್ನು ಶೇಖರಿಸಿಡುವ ವ್ಯವಸ್ಥೆ.",
                englishExplanation = "A data structure consisting of a collection of elements, each identified by at least one array index or key.",
                example = "The function returns an array of integers.",
                pronunciation = "/əˈrā/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Cybersecurity",
                kannadaMeaning = "ಸೈಬರ್ ಸುರಕ್ಷತೆ (Cyber Surakshete)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್ ವ್ಯವಸ್ಥೆಗಳು ಮತ್ತು ನೆಟ್‌ವರ್ಕ್‌ಗಳನ್ನು ದಾಳಿಗಳಿಂದ ರಕ್ಷಿಸುವ ವಿಧಾನ.",
                englishExplanation = "The state of being protected against the criminal or unauthorized use of electronic data.",
                example = "Cybersecurity is a top priority for governments worldwide.",
                pronunciation = "/ˌsībər-sə-ˈkyu̇r-ə-tē/",
                subject = "Computer Science"
            ),
            // --- More Batch ---
            TermEntity(
                englishWord = "Prime Number",
                kannadaMeaning = "ಅವಿಭಾಜ್ಯ ಸಂಖ್ಯೆ (Avibhajya Sankhye)",
                kannadaExplanation = "ಕೇವಲ 1 ಮತ್ತು ತನ್ನಿಂದ ಮಾತ್ರ ಭಾಗಿಸಲ್ಪಡುವ ಸಂಖ್ಯೆ.",
                englishExplanation = "A natural number greater than 1 that has no positive divisors other than 1 and itself.",
                example = "2, 3, 5, and 7 are the first few prime numbers.",
                pronunciation = "/prīm ˈnəmbər/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Fibonacci Sequence",
                kannadaMeaning = "ಫಿಬೋನಾಕಿ ಸರಣಿ (Fibonacci Sarani)",
                kannadaExplanation = "ಪ್ರತಿ ಸಂಖ್ಯೆಯು ಹಿಂದಿನ ಎರಡು ಸಂಖ್ಯೆಗಳ ಮೊತ್ತವಾಗಿರುವ ಸರಣಿ.",
                englishExplanation = "A series of numbers in which each number is the sum of the two preceding ones.",
                example = "The starting of the Fibonacci sequence is 0, 1, 1, 2, 3, 5...",
                pronunciation = "/ˌfibəˈnäCHē ˈsēkwəns/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Matrix",
                kannadaMeaning = "ಮಾತೃಕೆ (Matruke)",
                kannadaExplanation = "ಸಂಖ್ಯೆಗಳ ಆಯತಾಕಾರದ ಜೋಡಣೆ.",
                englishExplanation = "A rectangular array of quantities or expressions in rows and columns that is treated as a single entity.",
                example = "Matrices are used in computer graphics to transform objects.",
                pronunciation = "/ˈmātriks/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Ratio",
                kannadaMeaning = "ಅನುಪಾತ (Anupata)",
                kannadaExplanation = "ಎರಡು ಪ್ರಮಾಣಗಳ ನಡುವಿನ ಹೋಲಿಕೆ.",
                englishExplanation = "The quantitative relation between two amounts showing the number of times one value contains or is contained within the other.",
                example = "The ratio of boys to girls in the class is 2:3.",
                pronunciation = "/ˈrāSHēˌō/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Proportion",
                kannadaMeaning = "ಸಮಾನುಪಾತ (Samanupata)",
                kannadaExplanation = "ಎರಡು ಅನುಪಾತಗಳು ಸಮನಾಗಿರುವ ಸ್ಥಿತಿ.",
                englishExplanation = "A part, share, or number considered in comparative relation to a whole.",
                example = "The proportion of water to sand should be even.",
                pronunciation = "/prəˈpôrSH(ə)n/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Percentage",
                kannadaMeaning = "ಶೇಕಡಾವಾರು (Shekadavaru)",
                kannadaExplanation = "ನೂರಕ್ಕೆ ಎಷ್ಟು ಎಂದು ಲೆಕ್ಕ ಹಾಕುವ ವಿಧಾನ.",
                englishExplanation = "A rate, number, or amount in each hundred.",
                example = "He scored 90 percentage in the examination.",
                pronunciation = "/pərˈsen(t)ij/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Atom",
                kannadaMeaning = "ಪರಮಾಣು (Paramāṇu)",
                kannadaExplanation = "ದ್ರವ್ಯದ ಅತಿ ಸಣ್ಣ ಕಣ.",
                englishExplanation = "The basic unit of a chemical element.",
                example = "An atom consists of a nucleus surrounded by electrons.",
                pronunciation = "/ˈadəm/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Molecule",
                kannadaMeaning = "ಅಣು (Aṇu)",
                kannadaExplanation = "ಎರಡು ಅಥವಾ ಹೆಚ್ಚಿನ ಪರಮಾಣುಗಳ ಗುಂಪು.",
                englishExplanation = "A group of atoms bonded together, representing the smallest fundamental unit of a chemical compound.",
                example = "A water molecule consists of two hydrogen atoms and one oxygen atom.",
                pronunciation = "/ˈmäləˌkyo͞ol/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Proton",
                kannadaMeaning = "ಪ್ರೋಟಾನ್ (Proton)",
                kannadaExplanation = "ಪರಮಾಣುವಿನ ಕೇಂದ್ರದಲ್ಲಿರುವ ಸಕಾರಾತ್ಮಕ ಚಾರ್ಜ್ ಹೊಂದಿರುವ ಕಣ.",
                englishExplanation = "A stable subatomic particle occurring in all atomic nuclei, with a positive electric charge.",
                example = "The number of protons determines the atomic number of an element.",
                pronunciation = "/ˈprōtän/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Electron",
                kannadaMeaning = "ಎಲೆಕ್ಟ್ರಾನ್ (Electron)",
                kannadaExplanation = "ಪರಮಾಣುವಿನ ಕೇಂದ್ರದ ಸುತ್ತ ಸುತ್ತುವ ನಕಾರಾತ್ಮಕ ಚಾರ್ಜ್ ಹೊಂದಿರುವ ಕಣ.",
                englishExplanation = "A stable subatomic particle with a charge of negative electricity, found in all atoms.",
                example = "Electricity is the flow of electrons through a conductor.",
                pronunciation = "/əˈlekträn/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Neutron",
                kannadaMeaning = "ನ್ಯೂಟ್ರಾನ್ (Neutron)",
                kannadaExplanation = "ಪರಮಾಣುವಿನ ಕೇಂದ್ರದಲ್ಲಿರುವ ಯಾವುದೇ ಚಾರ್ಜ್ ಇಲ್ಲದ ಕಣ.",
                englishExplanation = "A subatomic particle of about the same mass as a proton but without an electric charge.",
                example = "Neutrons and protons together form the nucleus.",
                pronunciation = "/ˈn(y)o͞oträn/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Galaxy",
                kannadaMeaning = "ಗ್ಯಾಲಕ್ಸಿ/ಆಕಾಶಗಂಗೆ (Galaxy)",
                kannadaExplanation = "ಕೋಟಿಗಟ್ಟಲೆ ನಕ್ಷತ್ರಗಳು ಮತ್ತು ಗ್ರಹಗಳ ಸಮೂಹ.",
                englishExplanation = "A system of millions or billions of stars, together with gas and dust, held together by gravitational attraction.",
                example = "We live in the Milky Way galaxy.",
                pronunciation = "/ˈɡaləksē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Universe",
                kannadaMeaning = "ಬ್ರಹ್ಮಾಂಡ/ವಿಶ್ವ (Brahmanda/Vishwa)",
                kannadaExplanation = "ಎಲ್ಲಾ ಅಸ್ತಿತ್ವದಲ್ಲಿರುವ ವಸ್ತುಗಳು, ಶಕ್ತಿ ಮತ್ತು ಸ್ಥಳದ ಒಟ್ಟು ಮೊತ್ತ.",
                englishExplanation = "All existing matter and space considered as a whole; the cosmos.",
                example = "The universe is continuously expanding.",
                pronunciation = "/ˈyo͞onəˌvərs/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Budget",
                kannadaMeaning = "ಆಯವ್ಯಯ (Ayavyaya)",
                kannadaExplanation = "ನಿಗದಿತ ಅವಧಿಗೆ ಆದಾಯ ಮತ್ತು ವೆಚ್ಚದ ಅಂದಾಜು ಪಟ್ಟಿ.",
                englishExplanation = "An estimate of income and expenditure for a set period of time.",
                example = "The government presents the annual budget in February.",
                pronunciation = "/ˈbəjət/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Credit",
                kannadaMeaning = "ಜಮೆ/ಸಾಲ (Jame/Sala)",
                kannadaExplanation = "ನಂತರ ಪಾವತಿಸುವ ಭರವಸೆಯ ಮೇಲೆ ಪಡೆಯುವ ವಸ್ತು ಅಥವಾ ಹಣ.",
                englishExplanation = "The ability of a customer to obtain goods or services before payment, based on the trust that payment will be made in the future.",
                example = "She bought the car on credit.",
                pronunciation = "/ˈkredət/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Debit",
                kannadaMeaning = "ಖರ್ಚು (Kharchu)",
                kannadaExplanation = "ಖಾತೆಯಿಂದ ಹಣವನ್ನು ತೆಗೆಯುವುದು ಅಥವಾ ಪಾವತಿಸುವುದು.",
                englishExplanation = "An entry recording an amount owed, listed on the left-hand side or column of an account.",
                example = "The bank will debit the amount from your account tomorrow.",
                pronunciation = "/ˈdebət/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Interest",
                kannadaMeaning = "ಬಡ್ಡಿ (Baddi)",
                kannadaExplanation = "ಸಾಲ ಪಡೆದ ಹಣಕ್ಕೆ ಪ್ರತಿಯಾಗಿ ನೀಡುವ ಹೆಚ್ಚುವರಿ ಹಣ.",
                englishExplanation = "Money paid regularly at a particular rate for the use of money lent, or for delaying the repayment of a debt.",
                example = "High interest rates make loans expensive.",
                pronunciation = "/ˈintrəst/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "GDP",
                kannadaMeaning = "ಒಟ್ಟು ಆಂತರಿಕ ಉತ್ಪನ್ನ (GDP)",
                kannadaExplanation = "ಒಂದು ದೇಶದ ಒಟ್ಟು ಆರ್ಥಿಕ ಉತ್ಪಾದನೆಯ ಮೌಲ್ಯ.",
                englishExplanation = "Gross Domestic Product; the total value of goods produced and services provided in a country during one year.",
                example = "India's GDP growth is being watched by economists.",
                pronunciation = "/ˌjē-ˌdē-ˈpē/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Deflation",
                kannadaMeaning = "ಬೆಲೆ ಇಳಿಕೆ (Bele Ilike)",
                kannadaExplanation = "ಸರಕು ಮತ್ತು ಸೇವೆಗಳ ಬೆಲೆಗಳಲ್ಲಿನ ಸಾಮಾನ್ಯ ಇಳಿಕೆ.",
                englishExplanation = "Reduction of the general level of prices in an economy.",
                example = "Deflation can lead to reduced consumer spending.",
                pronunciation = "/dəˈflāSH(ə)n/",
                subject = "Commerce"
            ),
            TermEntity(
                englishWord = "Hardware",
                kannadaMeaning = "ಹಾರ್ಡ್‌ವೇರ್ (Hardware)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್‌ನ ಭೌತಿಕ ಭಾಗಗಳು (ಉದಾಹರಣೆಗೆ ಮೌಸ್, ಕೀಬೋರ್ಡ್).",
                englishExplanation = "The physical tools, machinery, and other durable equipment.",
                example = "The computer hardware needs an upgrade to run this game.",
                pronunciation = "/ˈhärdˌwer/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Software",
                kannadaMeaning = "ಸಾಫ್ಟ್‌ವೇರ್ (Software)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್ ನಿರ್ವಹಿಸಲು ನೀಡುವ ಮಾಹಿತಿಗಳು ಅಥವಾ ಪ್ರೋಗ್ರಾಂಗಳು.",
                englishExplanation = "The programs and other operating information used by a computer.",
                example = "We need to install the latest antivirus software.",
                pronunciation = "/ˈsôf(t)ˌwer/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Network",
                kannadaMeaning = "ಜಾಲ/ನೆಟ್‌ವರ್ಕ್ (Network)",
                kannadaExplanation = "ಮಾಹಿತಿ ಹಂಚಿಕೊಳ್ಳಲು ಒಂದಕ್ಕೊಂದು ಸಂಪರ್ಕ ಹೊಂದಿರುವ ಕಂಪ್ಯೂಟರ್‌ಗಳ ಗುಂಪು.",
                englishExplanation = "A group or system of interconnected people or things.",
                example = "The office network went down this morning.",
                pronunciation = "/ˈnetˌwərk/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Protocol",
                kannadaMeaning = "ಶಿಷ್ಟಾಚಾರ/ಪ್ರೋಟೋಕಾಲ್ (Protocol)",
                kannadaExplanation = "ಮಾಹಿತಿ ವಿನಿಮಯಕ್ಕಾಗಿ ಬಳಸುವ ನಿಯಮಗಳ ಗುಂಪು.",
                englishExplanation = "The official procedure or system of rules governing affairs of state or diplomatic occasions.",
                example = "HTTP is a protocol used for the World Wide Web.",
                pronunciation = "/ˈprōdəˌkôl/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "IP Address",
                kannadaMeaning = "ಐಪಿ ವಿಳಾಸ (IP Address)",
                kannadaExplanation = "ನೆಟ್‌ವರ್ಕ್‌ನಲ್ಲಿ ಪ್ರತಿಯೊಂದು ಕಂಪ್ಯೂಟರ್‌ಗೆ ನೀಡುವ ವಿಶಿಷ್ಟ ಗುರುತು.",
                englishExplanation = "Internet Protocol address; a unique string of numbers separated by periods that identifies each computer using the Internet Protocol to communicate over a network.",
                example = "Your IP address is used to track your location online.",
                pronunciation = "/ˌī ˈpē əˈdres/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Server",
                kannadaMeaning = "ಸರ್ವರ್ (Server)",
                kannadaExplanation = "ಮಾಹಿತಿಯನ್ನು ಶೇಖರಿಸಿಟ್ಟು ಇತರ ಕಂಪ್ಯೂಟರ್‌ಗಳಿಗೆ ನೀಡುವ ಪ್ರಬಲ ಕಂಪ್ಯೂಟರ್.",
                englishExplanation = "A computer or computer program which manages access to a centralized resource or service in a network.",
                example = "The web server handles thousands of requests per second.",
                pronunciation = "/ˈsərvər/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Browser",
                kannadaMeaning = "ಬ್ರೌಸರ್ (Browser)",
                kannadaExplanation = "ಇಂಟರ್ನೆಟ್‌ನಲ್ಲಿ ಮಾಹಿತಿಯನ್ನು ಹುಡುಕಲು ಬಳಸುವ ಸಾಫ್ಟ್‌ವೇರ್ (ಉದಾ: ಕ್ರೋಮ್).",
                englishExplanation = "A program with a graphical user interface for displaying HTML files, used to navigate the World Wide Web.",
                example = "Google Chrome is the most popular web browser.",
                pronunciation = "/ˈbrouzər/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Bandwidth",
                kannadaMeaning = "ಬ್ಯಾಂಡ್‌ವಿಡ್ತ್ (Bandwidth)",
                kannadaExplanation = "ನೆಟ್‌ವರ್ಕ್ ಸಂಪರ್ಕದಲ್ಲಿ ಒಂದು ಸಮಯದಲ್ಲಿ ಎಷ್ಟು ಮಾಹಿತಿ ಹರಿಯಬಹುದು ಎಂಬ ಸಾಮರ್ಥ್ಯ.",
                englishExplanation = "The range of frequencies within a given band, in particular that used for transmitting a signal.",
                example = "Video streaming requires high bandwidth.",
                pronunciation = "/ˈbandˌwidTH/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Firewall",
                kannadaMeaning = "ಫೈರ್‌ವಾಲ್ (Firewall)",
                kannadaExplanation = "ನೆಟ್‌ವರ್ಕ್ ಸುರಕ್ಷತೆಗಾಗಿ ಇರುವ ತಡೆಗೋಡೆ.",
                englishExplanation = "A part of a computer system or network that is designed to block unauthorized access while permitting outward communication.",
                example = "The firewall blocked a suspicious login attempt.",
                pronunciation = "/ˈfīərˌwôl/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Malware",
                kannadaMeaning = "ದುರುದ್ದೇಶಪೂರಿತ ಸಾಫ್ಟ್‌ವೇರ್ (Malware)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್‌ಗೆ ಹಾನಿ ಮಾಡಲು ತಯಾರಿಸಿದ ಕೆಟ್ಟ ಪ್ರೋಗ್ರಾಂಗಳು.",
                englishExplanation = "Software that is specifically designed to disrupt, damage, or gain unauthorized access to a computer system.",
                example = "Ransomware is a dangerous type of malware.",
                pronunciation = "/ˈmalˌwer/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Cryptography",
                kannadaMeaning = "ಗೂಢಲಿಪಿಶಾಸ್ತ್ರ (Gudhalipishastra)",
                kannadaExplanation = "ಮಾಹಿತಿಯನ್ನು ಸುರಕ್ಷಿತವಾಗಿಡಲು ಗುಪ್ತ ಸಂಕೇತಗಳನ್ನು ಬಳಸುವ ಕಲೆ.",
                englishExplanation = "The art of writing or solving codes.",
                example = "Cryptography is essential for blockchain technology.",
                pronunciation = "/kripˈtäɡrəfē/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Artificial Intelligence",
                kannadaMeaning = "ಕೃತಕ ಬುದ್ಧಿಮತ್ತೆ (Krutaka Buddhimatte)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್‌ಗಳು ಮನುಷ್ಯನಂತೆ ಯೋಚಿಸುವಂತೆ ಮಾಡುವ ತಂತ್ರಜ್ಞಾನ.",
                englishExplanation = "The theory and development of computer systems able to perform tasks normally requiring human intelligence.",
                example = "AI is used in self-driving cars.",
                pronunciation = "/ˌärdəˈfiSHəl inˈteləjəns/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Big Data",
                kannadaMeaning = "ಬೃಹತ್ ದತ್ತಾಂಶ (Bruhat Dattansha)",
                kannadaExplanation = "ಅತಿ ದೊಡ್ಡ ಪ್ರಮಾಣದ ಮತ್ತು ಸಂಕೀರ್ಣವಾದ ಮಾಹಿತಿ ಸಂಗ್ರಹ.",
                englishExplanation = "Extremely large data sets that may be analyzed computationally to reveal patterns, trends, and associations.",
                example = "Companies use big data to understand consumer behavior.",
                pronunciation = "/biɡ ˈdādə/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Cloud Computing",
                kannadaMeaning = "ಕ್ಲೌಡ್ ಕಂಪ್ಯೂಟಿಂಗ್ (Cloud Computing)",
                kannadaExplanation = "ಇಂಟರ್ನೆಟ್ ಮೂಲಕ ಸರ್ವರ್‌ಗಳು ಮತ್ತು ಡೇಟಾವನ್ನು ಬಳಸುವ ವಿಧಾನ.",
                englishExplanation = "The practice of using a network of remote servers hosted on the Internet to store, manage, and process data.",
                example = "AWS is a leading provider of cloud computing services.",
                pronunciation = "/kloud kəmˈpyo͞odiNG/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Machine Learning",
                kannadaMeaning = "ಯಂತ್ರ ಕಲಿಕೆ (Yantra Kalike)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್‌ಗಳು ಸ್ವತಃ ಕಲಿಯುವಂತೆ ಮಾಡುವ ಎಐ ನ ಒಂದು ಭಾಗ.",
                englishExplanation = "A type of artificial intelligence that allows software applications to become more accurate at predicting outcomes without being explicitly programmed.",
                example = "Machine learning models improve with more data.",
                pronunciation = "/məˈSHēn ˈlərniNG/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Quantum Computing",
                kannadaMeaning = "ಕ್ವಾಂಟಮ್ ಕಂಪ್ಯೂಟಿಂಗ್ (Quantum Computing)",
                kannadaExplanation = "ಕ್ವಾಂಟಮ್ ಭೌತಶಾಸ್ತ್ರದ ನಿಯಮಗಳನ್ನು ಬಳಸಿ ಕೆಲಸ ಮಾಡುವ ಅತಿ ವೇಗದ ಕಂಪ್ಯೂಟರ್ ತಂತ್ರಜ್ಞಾನ.",
                englishExplanation = "Computing using quantum-mechanical phenomena, such as superposition and entanglement.",
                example = "Quantum computing could revolutionize drug discovery.",
                pronunciation = "/ˈkwän(t)əm kəmˈpyo͞odiNG/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Blockchain",
                kannadaMeaning = "ಬ್ಲಾಕ್‌ಚೈನ್ (Blockchain)",
                kannadaExplanation = "ಕೇಂದ್ರೀಕೃತ ವ್ಯವಸ್ಥೆ ಇಲ್ಲದೆ ಮಾಹಿತಿ ದಾಖಲಿಸುವ ಸುರಕ್ಷಿತ ವಿಧಾನ.",
                englishExplanation = "A system in which a record of transactions made in bitcoin or another cryptocurrency are maintained across several computers that are linked in a peer-to-peer network.",
                example = "Blockchain is the technology behind Bitcoin.",
                pronunciation = "/ˈbläkˌCHān/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Virtual Reality",
                kannadaMeaning = "ವರ್ಚುವಲ್ ರಿಯಾಲಿಟಿ (Virtual Reality)",
                kannadaExplanation = "ಕಂಪ್ಯೂಟರ್ ರಚಿಸಿದ ಕೃತಕ ಪ್ರಪಂಚದ ಅನುಭವ.",
                englishExplanation = "The computer-generated simulation of a three-dimensional image or environment that can be interacted with in a seemingly real or physical way.",
                example = "VR headsets provide an immersive gaming experience.",
                pronunciation = "/ˈvərCH(o͞o)əl rēˈalədē/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Augmented Reality",
                kannadaMeaning = "ಆಗ್ಮೆಂಟೆಡ್ ರಿಯಾಲಿಟಿ (Augmented Reality)",
                kannadaExplanation = "ೈಜ ಪ್ರಪಂಚದ ಮೇಲೆ ಡಿಜಿಟಲ್ ಮಾಹಿತಿಯನ್ನು ಸೇರಿಸುವ ತಂತ್ರಜ್ಞಾನ.",
                englishExplanation = "A technology that superimposes a computer-generated image on a user's view of the real world, thus providing a composite view.",
                example = "Pokemon Go is a famous example of AR.",
                pronunciation = "/ôɡˈmented rēˈalədē/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Internet of Things",
                kannadaMeaning = "ಐಒಟಿ/ವಸ್ತುಗಳ ಅಂತರ್ಜಾಲ (IoT)",
                kannadaExplanation = "ಪರಸ್ಪರ ಸಂಪರ್ಕ ಹೊಂದಿರುವ ಸಾಮಾನ್ಯ ಗೃಹೋಪಯೋಗಿ ವಸ್ತುಗಳ ಜಾಲ.",
                englishExplanation = "The network of physical objects programmed with sensors, software, and other technologies for the purpose of connecting and exchanging data with other devices over the internet.",
                example = "Smart bulbs are part of the Internet of Things.",
                pronunciation = "/ˈin(t)ərˌnet əv THiNGz/",
                subject = "Computer Science"
            ),
            TermEntity(
                englishWord = "Biodiversity",
                kannadaMeaning = "ಜೀವವೈವಿಧ್ಯ (Jivavaividhya)",
                kannadaExplanation = "ಒಂದು ನಿರ್ದಿಷ್ಟ ಪ್ರದೇಶದಲ್ಲಿ ಕಂಡುಬರುವ ವಿವಿಧ ರೀತಿಯ ಜೀವಿಗಳ ಸಮೂಹ.",
                englishExplanation = "The variety of plant and animal life in the world or in a particular habitat.",
                example = "Tropical rainforests have high levels of biodiversity.",
                pronunciation = "/ˌbīōˌdəˈvərsədē/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Ecosystem",
                kannadaMeaning = "ಪರಿಸರ ವ್ಯವಸ್ಥೆ (Parisara Vyavasthe)",
                kannadaExplanation = "ಜೀವಿಗಳು ಮತ್ತು ಅವುಗಳ ಸುತ್ತಮುತ್ತಲಿನ ಅಜೈವಿಕ ಪರಿಸರದ ನಡುವಿನ ಪರಸ್ಪರ ಕ್ರಿಯೆ.",
                englishExplanation = "A biological community of interacting organisms and their physical environment.",
                example = "A pond is a small freshwater ecosystem.",
                pronunciation = "/ˈēkōˌsistəm/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "DNA",
                kannadaMeaning = "ಡಿಎನ್‌ಎ (DNA)",
                kannadaExplanation = "ಜೀವಿಗಳ ಆನುವಂಶಿಕ ಮಾಹಿತಿಯನ್ನು ಹೊತ್ತಿರುವ ಅಣು.",
                englishExplanation = "Deoxyribonucleic acid; a self-replicating material which is the main constituent of chromosomes. It is the carrier of genetic information.",
                example = "DNA testing can identify genetic disorders.",
                pronunciation = "/ˌdē-ˌen-ˈā/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Chromosome",
                kannadaMeaning = "ವರ್ಣತಂತು (Varnatantu)",
                kannadaExplanation = "ಜೀವಕೋಶದ ಕೇಂದ್ರದಲ್ಲಿರುವ ಡಿಎನ್‌ಎ ಹೊಂದಿರುವ ದಾರದಂತಹ ರಚನೆ.",
                englishExplanation = "A threadlike structure of nucleic acids and protein found in the nucleus of most living cells, carrying genetic information in the form of genes.",
                example = "Humans have 23 pairs of chromosomes.",
                pronunciation = "/ˈkrōməˌsōm/",
                subject = "Science"
            ),
            TermEntity(
                englishWord = "Theorem",
                kannadaMeaning = "ಪ್ರಮೇಯ (Prameya)",
                kannadaExplanation = "ತಾರ್ಕಿಕವಾಗಿ ಸಾಬೀತಾದ ಗಣಿತದ ಹೇಳಿಕೆ.",
                englishExplanation = "A general proposition not self-evident but proved by a chain of reasoning; a truth established by means of accepted truths.",
                example = "Pythagoras' theorem is fundamental in geometry.",
                pronunciation = "/ˈTHirəm/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Axiom",
                kannadaMeaning = "ಸ್ವಯಂಸಿದ್ಧಾಂತ (Svayamsiddhanta)",
                kannadaExplanation = "ಯಾವುದೇ ಸಾಬೀತು ಇಲ್ಲದೆ ಸತ್ಯವೆಂದು ಒಪ್ಪಿಕೊಳ್ಳುವ ಮೂಲ ನಿಯಮ.",
                englishExplanation = "A statement or proposition that is regarded as being established, accepted, or self-evidently true.",
                example = "Euclidean geometry is built on several basic axioms.",
                pronunciation = "/ˈaksēəm/",
                subject = "Mathematics"
            ),
            TermEntity(
                englishWord = "Conjecture",
                kannadaMeaning = "ಅಂದಾಜು/ಊಹೆ (Andaju/Uhe)",
                kannadaExplanation = "ಸತ್ಯವೆಂದು ತೋರುವ ಆದರೆ ಇನ್ನೂ ಸಾಬೀತಾಗದ ಗಣಿತದ ಹೇಳಿಕೆ.",
                englishExplanation = "An opinion or conclusion formed on the basis of incomplete information.",
                example = "The Goldbach conjecture is one of the oldest unsolved problems in number theory.",
                pronunciation = "/kənˈjekCHər/",
                subject = "Mathematics"
            )
        )
        dao.insertAll(initialTerms)
    }
}
