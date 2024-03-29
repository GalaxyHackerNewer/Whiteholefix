/*
    © 2012 - 2019 - Whitehole Team

    Whitehole is free software: you can redistribute it and/or modify it under
    the terms of the GNU General Public License as published by the Free
    Software Foundation, either version 3 of the License, or (at your option)
    any later version.

    Whitehole is distributed in the hope that it will be useful, but WITHOUT ANY 
    WARRANTY; See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with Whitehole. If not, see http://www.gnu.org/licenses/.
*/

package whitehole.io;

import java.io.*;
import java.util.*;
public class ExternalFilesystem implements FilesystemBase {
    public ExternalFilesystem(String basedir) throws IOException {
        if (basedir.endsWith("/") || basedir.endsWith("\\"))
            basedir = basedir.substring(0, basedir.length() - 1);

        baseDirectory = new File(basedir);
        if (!baseDirectory.exists()) throw new IOException("Directory '" + basedir + "' doesn't exist");
        if (!baseDirectory.isDirectory()) throw new IOException(basedir + " isn't a directory");
    }
    
    @Override
    public void save() {}
    
    @Override
    public void close() {}


    @Override
    public List<String> getDirectories(String directory) {
        directory = directory.substring(1);
        
        File[] files = new File(baseDirectory, directory).listFiles();
        List<String> ret = new ArrayList<>();
        try {
            for (File file: files) {
                // file might be part of mod folder
                file = new File(getFileName(file.getAbsolutePath().substring(baseDirectory.getAbsolutePath().length())));
                
                if (!file.isDirectory())
                    continue;
                
                ret.add(file.getName());
            }
        } catch(Exception ex) {
            System.err.println(ex);
        }

        return ret;
    }

    @Override
    public boolean directoryExists(String directory) {
        directory = directory.substring(1);
        
        File dir = new File(getFileName(directory));
        
        return dir.exists() && dir.isDirectory();
    }


    @Override
    public List<String> getFiles(String directory) {
        directory = directory.substring(1);
        
        File[] files = new File(baseDirectory, directory).listFiles();
        List<String> ret = new ArrayList<>();

        for (File file: files)
        {
            // file might be part of mod folder
            file = new File(getFileName(file.getAbsolutePath().substring(baseDirectory.getAbsolutePath().length())));
            
            if (!file.isFile())
                continue;
            
            ret.add(file.getName());
        }

        return ret;
    }

    @Override
    public boolean fileExists(String filename) { 
        //filename = filename.substring(1);
        
        File file = new File(getFileName(filename));
        return file.exists() && file.isFile();
    }

    @Override
    public FileBase openFile(String filename) throws IOException {
        if (!fileExists(filename))
            throw new FileNotFoundException("File '" + filename + "' doesn't exist");
        
        return new ExternalFile(baseDirectory.getAbsolutePath() + filename);
    }
    
    @Override
    public void createFile(String parent, String newfile) throws IOException {
        if(!parent.startsWith(baseDirectory.getAbsolutePath()))
            parent = baseDirectory.getAbsolutePath() + parent;
        
        if(!parent.endsWith("/") && !parent.startsWith("/"))
            parent += "/";
        
        new File(parent).mkdir();
        
        new File(parent + newfile).createNewFile();
    }

    @Override
    public void renameFile(String file, String newname) {
        throw new UnsupportedOperationException("not done lol");
    }

    @Override
    public void deleteFile(String file) {
        throw new UnsupportedOperationException("not done lol");
    }


    private final File baseDirectory;

    @Override
    public void createDirectory(String parent, String newdir) {
        throw new UnsupportedOperationException("not done lol");
    }

    @Override
    public void renameDirectory(String file, String newname) throws FileNotFoundException {
        throw new UnsupportedOperationException("not done lol");
    }

    @Override
    public void deleteDirectory(String dir) {
        throw new UnsupportedOperationException("not done lol");
    }
    
    public String getFileName(String file) {
        return baseDirectory.getAbsolutePath() + file;
    }
}
