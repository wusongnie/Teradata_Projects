package songnie.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 

/**
 * This is the WebServlet that handles the file uploading.
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     
    // the directory for the uploaded files
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // the size bounds of the uploaded files
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    /**
     * uploading and saving the files
     */
    
    protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		// check if is multi part content
		if (!ServletFileUpload.isMultipartContent(request)) {
		    // if not, halt
		    PrintWriter writer = response.getWriter();
		    writer.println("Error: the form must contain enctype=multipart/form-data");
		    writer.flush();
		    return;
		}
 
        // use the new file factory
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // set the memory threshold
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // set the temporary directory
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
 
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // set the maximum file size
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        upload.setHeaderEncoding("UTF-8"); 

        // the path to store the uploaded file
        String uploadPath = getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
       
         
        // construct if path does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
 
        try {
            
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                
                for (FileItem item : formItems) {
                    
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // print the path to the console
                        System.out.println(filePath);
                        // save the file to the disk
                        item.write(storeFile);
                        request.setAttribute("message",
                            "File successfully uploaded!");
                        
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "Error: " + ex.getMessage());
        }
        // jump to message.jsp
        getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
        
    }
    
}