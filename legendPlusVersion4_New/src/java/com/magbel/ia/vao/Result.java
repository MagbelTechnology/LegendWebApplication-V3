package com.magbel.ia.vao;

import java.io.Serializable;

public class Result
    implements Serializable
{

    //private static final long serialVersionUID = 1L;
    private String mtId;
    private String admin_no;
    private String companyCode;
    private String program;
    private String branch;  
    private String session;
    private String semester;
    private String courses;
    private String crschoolcode;
    private String batch;
    private String status;
    private int userId;
    private double totalScore;
    private double percentage;
    private double result;
    private double examscore;
    private double cascore;    
    private String createdate;
    private String subjectcode;
    private String markedby;
    private String description;
    private String classarm;
    private String positionbyarm;
   public Result()
    {
          
    }

	public Result(String mtId,String admin_no,String companyCode,String program, String status,
                          String branch, String session,String semester, String courses, 
                          String crschoolcode, String batch,int userId,double totalScore,
                          double percentage,double result,double examscore,double cascore,
                          String subjectcode,String markedby,String createdate) {
	
        this.mtId = mtId;
        this.admin_no = admin_no;
		this.companyCode = companyCode;
		this.program = program;
		this.status = status;		
		this.branch = branch;
		this.session = session;
		this.semester = semester;
		this.courses = courses;
		this.crschoolcode = crschoolcode;
		this.batch = batch;
        this.userId = userId;
        this.totalScore = totalScore;
        this.percentage = percentage;
        this.result = result;
        this.examscore = examscore;
        this.cascore = cascore;    
        this.subjectcode = subjectcode;
        this.createdate = createdate;   
        this.markedby = markedby;
	}
    public final String getMtId()
    {
        return mtId;
    }

    public final void setMtId(String MtId)
    {
        mtId = MtId;
    }
    public final String getAdmin_no()
    {
        return admin_no;
    }

    public final void setAdmin_no(String admin_no)
    {
        this.admin_no = admin_no;
    }    
    public final String getCompanyCode()
    {
        return companyCode;
    }

    public final void setCompanyCode(String companyCode)
    {
        this.companyCode = companyCode;
    }    

    public final String getProgram()
    {
        return program;
    }

    public final void setProgram(String Program)
    {
    	program = Program;
    }
    public final String getBranch()
    {
        return branch;
    }

    public final void setBranch(String Branch)
    {
    	branch = Branch;
    }

    public final String getSession()
    {
        return session;
    }

    public final void setSession(String Session)
    {
    	session = Session;
    }

    public final String getSemester()
    {
        return semester;
    }

    public final void setSemester(String Semester)
    {
    	semester = Semester;
    }
    public final String getCourses()
    {
        return courses;
    }

    public final void setCourses(String Courses)
    {
    	courses = Courses;
    }
    public final String getCrschoolcode()
    {
        return crschoolcode;
    }

    public final void setCrschoolcode(String Crschoolcode)
    {
    	crschoolcode = Crschoolcode;
    }    
    public final String getBatch()
    {
        return batch;
    }

    public final void setBatch(String Batch)
    {
    	batch = Batch;
    }
    public final String getStatus()
    {
        return status;
    }
    public final double getTotalScore()
    {
        return totalScore;
    }

    public final void setTotalScore(double totalScore)
    {
        this.totalScore = totalScore;
    }    
    public final double getPercentage()
    {
        return percentage;
    }

    public final void setPercentage(double percentage)
    {
        this.percentage = percentage;
    }  
    public final double getExamscore()
    {
        return examscore;
    }

    public final void setExamscore(double examscore)
    {
        this.examscore = examscore;
    }    
    public final double getResult()
    {
        return result;
    }

    public final void setResult(double result)
    {
        this.result = result;
    }    
    public final double getCascore()
    {
        return cascore;
    }

    public final void setCascore(double cascore)
    {
        this.cascore = cascore;
    }        
    public final String getCreatedate()
    {
        return createdate;
    }

    public final void setCreatedate(String Createdate)
    {
    	createdate = Createdate;
    }
    public final int getUserId()
    {
        return userId;
    }
    public final String getSubjectcode()
    {
        return subjectcode;
    }

    public final void setSubjectcode(String Subjectcode)
    {
    	subjectcode = Subjectcode;
    }    
    public final String getMarkedby()
    {
        return markedby;
    }

    public final void setMarkedby(String Markedby)
    {
    	markedby = Markedby;
    }        
    
    public final void setUserId(int UserId)
    {
        userId = UserId;
    }
    
    public final String getDescription()
    {
        return description;
    }

    public final void setDescription(String Description)
    {
    	description = Description;
    }   
    
    public final String getClassarm()
    {
        return classarm;
    }

    public final void setClassarm(String Classarm)
    {
    	classarm = Classarm;
    } 
     
    public final String getPositionbyarm()
    {
        return positionbyarm;
    }

    public final void setPositionbyarm(String Positionbyarm)
    {
    	positionbyarm = Positionbyarm;
    }   
    
}
