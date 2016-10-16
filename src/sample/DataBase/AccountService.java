package sample.DataBase;


public interface AccountService {

    /*
    * Retrieves current balance or zero if balance was not set
    *
    * @param id balance (user) identifier
    */
    Long getAmount(Integer id) throws Exception;

    /*
    * Increases balance or set if addAmount() method was called first time for this id
    *
    * @param id balance (user) identifier
    * @param value positive or negative, will be added to the current balance whose owner is id
    */
    void addAmount(Integer id, Long amount) throws Exception;

    /*
    * The method below is used in order to collect the statistics about the running time of each command
    */

    long getRunTimeOfLastExecutedCommand();

}
