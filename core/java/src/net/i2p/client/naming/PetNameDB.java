package net.i2p.client.naming;

import java.io.*;
import java.util.*;


/**
 *
 */
public class PetNameDB {
    /** name (String) to PetName mapping */
    private Map _names;
    private String _path;
    
    public PetNameDB() {
        _names = Collections.synchronizedMap(new HashMap());
    }

    public PetName getName(String name) { return (PetName)_names.get(name); }
    public void add(PetName pn) { _names.put(pn.getName(), pn); }
    public void clear() { _names.clear(); }
    public boolean contains(PetName pn) { return _names.containsValue(pn); }
    public boolean containsName(String name) { return _names.containsKey(name); }
    public boolean isEmpty() { return _names.isEmpty(); }
    public Iterator iterator() { return new LinkedList(_names.values()).iterator(); }
    public void remove(PetName pn) { _names.values().remove(pn); }
    public void removeName(String name) { _names.remove(name); }
    public int size() { return _names.size(); }
    public Set getNames() { return new HashSet(_names.keySet()); }
    public List getGroups() {
        List rv = new ArrayList();
        for (Iterator iter = iterator(); iter.hasNext(); ) {
            PetName name = (PetName)iter.next();
            for (int i = 0; i < name.getGroupCount(); i++)
                if (!rv.contains(name.getGroup(i)))
                    rv.add(name.getGroup(i));
        }
        return rv;
    }
    
    public PetName getLocation(String location) { 
        if (location == null) return null;
        synchronized (_names) {
            for (Iterator iter = iterator(); iter.hasNext(); ) {
                PetName name = (PetName)iter.next();
                if ( (name.getLocation() != null) && (name.getLocation().trim().equals(location.trim())) )
                    return name;
            }
        }
        return null;
    }
    
    public void load(String location) throws IOException {
        _path = location;
        File f = new File(location);
        if (!f.exists()) return;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = null;
            while ( (line = in.readLine()) != null) {
                PetName name = new PetName(line);
                if (name.getName() != null)
                    add(name);
            }
        } finally {
            in.close();
        }
    }
    
    public void store(String location) throws IOException {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(location), "UTF-8");
            for (Iterator iter = iterator(); iter.hasNext(); ) {
                PetName name = (PetName)iter.next();
                if (name != null)
                    out.write(name.toString() + "\n");
            }
        } finally {
            out.close();
        }
    }
    
    public void store() throws IOException {
        if (_path != null) {
            store(_path);
        }
    }
}
