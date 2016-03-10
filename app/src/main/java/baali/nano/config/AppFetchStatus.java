package baali.nano.config;

/**
 * Created by Balaji on 10/03/16.
 */
public class AppFetchStatus
{
    private static MovieFetchOptions state;

    public static MovieFetchOptions getState()
    {
        return state;
    }

    public static void setState(MovieFetchOptions state)
    {
        AppFetchStatus.state = state;
    }
}
