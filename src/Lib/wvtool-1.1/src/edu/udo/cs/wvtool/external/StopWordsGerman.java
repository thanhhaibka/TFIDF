package edu.udo.cs.wvtool.external;

import java.util.Hashtable;

/**
 * Stopwords for the German language
 * 
 * @version $Id$
 *
 */
public class StopWordsGerman {

    /** The hashtable containing the list of stopwords */
    private static Hashtable m_Stopwords = null;

    private static String[] stopWords = new String[] { "ab", "aber", "Aber", "alle", "allein", "allem", "allen",
            "aller", "als", "Als", "also", "alt", "am", "Am", "an", "andere", "anderen", "arbeiten", "auch", "Auch",
            "auf", "Auf", "Aufgabe", "aus", "au�er", "bald", "beginnen", "bei", "Bei", "beide", "beiden", "beim",
            "bekannt", "bekennen", "bereits", "berichten", "bestehen", "betonen", "betonte", "bin", "bis", "bi�chen",
            "bisschen", "Bisschen", "bist", "bleiben", "bringen", "da", "dabei", "dadurch", "daf�r", "dagegen",
            "dahinter", "damit", "danach", "daneben", "dann", "daran", "darauf", "daraus", "darin", "dar�ber", "darum",
            "darunter", "das", "Das", "da�", "dass", "Dass", "dasselbe", "davon", "davor", "dazu", "dazwischen",
            "dein", "deine", "deinem", "deinen", "deiner", "deines", "dem", "demselben", "den", "denen", "denn", "der",
            "Der", "deren", "derselben", "des", "desselben", "dessen", "deutlich", "dich", "die", "Die", "dies",
            "Dies", "diese", "Diese", "dieselbe", "dieselben", "diesem", "diesen", "dieser", "dieses", "dir", "doch",
            "Doch", "dort", "drei", "du", "durch", "d�rfen", "ebenso", "eigen", "eigenen", "ein", "Ein", "eine",
            "Eine", "einem", "einen", "einer", "eines", "einig", "einige", "einigen", "einmal", "entlang",
            "entscheiden", "entsprechen", "EPD", "er", "Er", "erhalten", "erkl�ren", "erkl�rte", "erst", "ersten",
            "es", "Es", "etwa", "etwas", "euch", "euer", "eure", "eurem", "euren", "eurer", "eures", "fest", "finden",
            "fordern", "fragen", "frei", "fr�h", "f�hren", "f�nf", "f�r", "F�r", "f�rs", "ganz", "gar", "gebe",
            "geben", "gegen", "gegen�ber", "gehen", "geh�ren", "geht", "gemeinsam", "genau", "gewesen", "gibt",
            "glauben", "gleich", "gro�", "gro�en", "gr�nden", "gut", "habe", "haben", "handeln", "hat", "hatte",
            "h�tte", "hatten", "h�tten", "heilig", "hei�t", "her", "herein", "herum", "heute", "hin", "hinter",
            "hintern", "hoch", "h�ren", "ich", "ihm", "ihn", "Ihnen", "ihnen", "ihr", "ihre", "Ihre", "ihrem", "Ihrem",
            "ihren", "Ihren", "Ihrer", "ihrer", "ihres", "Ihres", "im", "Im", "immer", "in", "In", "ins",
            "international", "ist", "ja", "je", "jedesmal", "jedoch", "jene", "jenem", "jenen", "jener", "jenes",
            "jetzt", "jung", "kann", "KAP", "kaum", "kein", "keine", "keinem", "keinen", "keiner", "keines",
            "kirchlich", "klein", "kommen", "k�nne", "k�nnen", "k�nnten", "kritisieren", "lang", "la�", "lass",
            "lassen", "leben", "letzen", "letzte", "letzten", "machen", "man", "mehr", "mein", "meine", "meinem",
            "meinen", "meiner", "meines", "meist", "mich", "mir", "mit", "Mit", "mitteilen", "m�glich", "mu�", "muss",
            "m�sse", "m�ssen", "m��ten", "m�ssten", "nach", "Nach", "nachdem", "nah", "n�mlich", "national", "neben",
            "nehmen", "nein", "nennen", "neu", "neue", "neuen", "nicht", "nichts", "noch", "nun", "nur", "ob", "ober",
            "obgleich", "oder", "ohne", "paar", "Recht", "recht", "reich", "religi�s", "rund", "sagte", "schaffen",
            "schon", "schreiben", "schwer", "sehen", "sehr", "sei", "seien", "sein", "seine", "seinem", "seinen",
            "seiner", "seines", "seit", "seitdem", "selbst", "Selbst", "setzen", "sich", "Sie", "sie", "sind", "so",
            "So", "sogar", "solch", "solche", "solchem", "solchen", "solcher", "solches", "soll", "sollen", "sollte",
            "sollten", "sondern", "sonst", "soviel", "soweit", "sowie", "sp�t", "sprechen", "stark", "stehen", "steht",
            "stellen", "teilen", "teilte", "�ber", "um", "und", "Und", "uns", "unser", "unsere", "unserem", "unseren",
            "unserer", "unseres", "unter", "vergangen", "vergangenen", "vergehen", "ver�ffentlichen", "viel", "viele",
            "vier", "voll", "vom", "von", "Von", "vor", "Vor", "vorsitzen", "w�hren", "w�hrend", "war", "w�re",
            "waren", "w�ren", "warum", "was", "wegen", "weil", "weit", "weiter", "welche", "welchem", "welchen",
            "welcher", "welches", "wem", "wen", "wenig", "wenige", "wenn", "Wenn", "wer", "werde", "werden", "weshalb",
            "wessen", "wichtig", "wie", "Wie", "wieder", "will", "wir", "Wir", "wird", "wo", "wollen", "womit",
            "worden", "wurde", "wurden", "w�rden", "zehn", "zeigen", "zentral", "zu", "Zu", "zum", "zur", "zwar",
            "zwei", "zweit", "zwischen", "zwischens" };

    static {
        if (m_Stopwords == null) {
            m_Stopwords = new Hashtable();

            Double dummy = new Double(0);
            for (int i = 0; i < stopWords.length; i++) {
                m_Stopwords.put(stopWords[i], dummy);
            }

        }

    }

    public boolean isStopword(String str) {

        return m_Stopwords.containsKey(str.toLowerCase());
    }

}
