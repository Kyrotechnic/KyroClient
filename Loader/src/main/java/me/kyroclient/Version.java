package me.kyroclient;

public class Version {
    public int build;
    public int majorRelease;
    public int minorRelease;

    public Version(String compiled)
    {
        System.out.println(compiled);
        String[] str = compiled.split("\\.");

        majorRelease = Integer.parseInt(str[0]);
        String[] str1 = str[1].split("-");

        minorRelease = Integer.parseInt(str1[0]);
        build = Integer.parseInt(str1[1].substring(1));
    }

    public Version(int majorRelease, int minorRelease, int build)
    {
        this.majorRelease = majorRelease;
        this.minorRelease = minorRelease;
        this.build = build;
    }

    public static boolean isUpdated(Version ver1, Version ver2)
    {
        if (ver2.majorRelease > ver1.majorRelease)
        {
            return false;
        }

        if (ver2.minorRelease > ver1.minorRelease)
        {
            return false;
        }

        if (ver2.build > ver1.build)
        {
            return false;
        }

        return true;
    }

    public String toString()
    {
        return String.format("%s.%s-b%s", majorRelease, minorRelease, build);
    }
}
