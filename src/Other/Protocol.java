package Other;

/*
 * 
 * Protokol sloužící pro komunikaci mezi klientem a serverem při hře piškvorek
 */

public final class Protocol {
    
    // client -> server
    public static final String USER_LOGIN ="101";         // pokus o login hrace s urcitym jmenem;       format: '101 username'
    public static final String PLACE_MARK = "102";       // pokus o vlozeni znacky na danou x/y pozici; format: '102 10,10'
    public static final String REGAME = "103";            // pokus o zahrani dalsi hry se souperem
    // server -> client
    public static final String LOGIN_SUCCESS = "601";
    public static final String LOGIN_FAIL = "602";          
    public static final String GAME_READY = "603";        // informace o tom, ze je je vse pripraveno ke hre (souper hlavne)
    public static final String POSITION_OCCUPIED = "610"; // informace o tom, ze dana pozice je jiz obsazena znackou 
    public static final String ENEMY_MARK_PLACED = "611"; // informace o polozeni znacky protihrace na pozici x/y;    format '610 5,6'
    public static final String YOUR_MARK_PLACED = "612";  // informace o polozeni vlastni znacky hrace na pozici x/y; format '611 5,5'
    public static final String NOT_YOUR_TURN = "614";     // informace o tom, ze hrac neni na tahu
    public static final String YOUR_TURN = "615";
    public static final String WIN = "620";               // informace o vyhre hry
    public static final String LOSS = "621";              // informace o prohre hry
    public static final String DRAW = "622";              // informace o remize
    public static final String NEW_GAME = "700";          // informace o tom, ze byla vycistena hraci plocha
    
    public static final String GENERAL_FAIL = "800";      // informace o tom, ze se na serveru stala chyba..
    
    private static String CORRUPTED_MESSAGE = "000";
    
    /*
     * Vrati substring s cislem zpravy protokolu. Pro zpravy, ktere neodpovidaji protokolu 
     * -> delkou kratsi nez tri vrati String, ktery nebude souhlasit se zadny cislem protokolu. To
     * je tak proto, aby pri zaslani prazdne zpravy nenastala vyjimka pri volani metody substring
     */
    public static String extractProtocolNum(String message) {
        return ((message.length() < 3) ? CORRUPTED_MESSAGE : message.substring(0, 3));
    }
    
    /*
     * Vrati substring s telem zpravy. To pri teto specifikaci protokolu mohou byt pouze souradnice
     * znacky. Osetreni delky zpravy viz. predchozi comment. Zprava, ktera neobsahuje za cislem
     * protokolu mezeru jeste projde. Spatny format pri spravne delce je osetren vyjmkami
     */
    public static String extractMessageBody(String message) {
        return ((message.length() < 7) ? CORRUPTED_MESSAGE : message.substring(4).trim());
    }
    
}


