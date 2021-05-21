import java.util.HashSet;

public class WorkerHandler 
{
    public static int numWorkers = 5;
    
    private Worker[] workers;
    private URLPool urlPool;
    private int maxDepth;

    public WorkerHandler(URLPool pool, int maxDepth)
    {
        this.urlPool = pool;
        this.maxDepth = maxDepth;

        this.workers = new Worker[numWorkers];
        for(int i = 0; i < this.workers.length; i++)
            this.workers[i] = new Worker();
    }
    public void startTask()
    {
        for(int i = 0; i < this.workers.length; i++)
            this.workers[i].start();
    }
    public boolean isEnd()
    {
        int waiting = 0;
        for(int i = 0; i < this.workers.length; i++)
        {
            if(this.workers[i].isWaiting() == true)
                waiting++;
        }
        return waiting == this.workers.length;
    }
    public void stopTasks()
    {
        for(int i = 0; i < this.workers.length; i++)
            this.workers[i].interrupt();
    }

    private class Worker extends Thread
    {
        private boolean isWaiting = false;
        public boolean isWaiting() { return this.isWaiting; }

        @Override
        public void run()
        {
            while(!isInterrupted())
            {
                try 
                {
                    this.isWaiting = true;

                    URLPair url = urlPool.getPair();
                    if(url == null)
                        continue;

                    this.isWaiting = false;
                    int currentDepth = url.getDepth();
                    if(currentDepth >= maxDepth) continue;
                    urlPool.insertVisited(url);

                    System.out.println("Parsing: " + url);
                    HTTPRequest request = new HTTPRequest(url);
                    String page = request.getPage();

                    HashSet<URLPair> urls = RegexParser.getUrlsFromPage(page, currentDepth + 1);
                    urlPool.insertToQueue(urls);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
    }
}