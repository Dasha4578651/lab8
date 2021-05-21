import java.util.HashSet;
import java.util.Iterator;

class URLPool
{
    private HashSet<URLPair> visited = new HashSet<>();     // Контейнер для посещённых сайтов
    private HashSet<URLPair> toVisit = new HashSet<>();     // Контейнер для сайтов, который надо посетить

    public synchronized URLPair getPair()
    {
        Iterator<URLPair> it = this.toVisit.iterator();
        if(!it.hasNext()) return null;

        URLPair ret = it.next();
        try { this.toVisit.remove(ret); }
        catch(Exception e) { System.out.println("Error: " + e); }

        return ret;
    }
    public synchronized boolean insertVisited(URLPair pair)
    {
        if(!this.visited.contains(pair))
        {
            visited.add(pair);
            return true;
        }
        return false;
    }
    public synchronized void insertToQueue(HashSet<URLPair> urlSet)
    {
        for(URLPair url : urlSet)
        {
            if(!this.visited.contains(url))
                this.toVisit.add(url);
        }
    }

    public int getQueueSize() { return this.toVisit.size(); }
    public HashSet<URLPair> getVisited() { return this.visited; }
}