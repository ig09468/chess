package logique;

import java.util.Objects;

public class HashRecord implements Comparable<HashRecord>{

    private long hash;
    private int count;

    public HashRecord(long hash) {
        this.hash = hash;
        this.count = 1;
    }

    public void inc()
    {
        count++;
    }

    public void dec()
    {
        count--;
    }

    public boolean isThreefold()
    {
        return count >=3;
    }

    public boolean isNever()
    {
        return count <=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashRecord that = (HashRecord) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, count);
    }

    @Override
    public HashRecord clone()
    {
        HashRecord hr = new HashRecord(hash);
        hr.count=count;
        return hr;
    }


    @Override
    public int compareTo(HashRecord o) {
        return Long.compare(this.hash, o.hash);
    }
}
