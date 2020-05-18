var crypto = require('crypto');
var express = require('express');
var uuid = require('uuid');
var mysql = require('mysql');
var bodyParser = require('body-parser');


var app = express()
app.use(bodyParser.json()); //JSON params
app.use(bodyParser.urlencoded({extended:true})); // accpet url encoded params


//============================================My SQL Connection=========================================//
var conn = mysql.createConnection({
    host:'remotemysql.com',
    user:'M1dLL4sKdM',
    password:'sEWth7V12F',
    database:'M1dLL4sKdM'
});

/*============================================== Register ========================================================*/


var getRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
                            .toString('hex')
                            .slice(0,length);

};

var sha512 = function(password,salt){
    var hash = crypto.createHmac('sha512',salt);
    hash.update(password);
    var value = hash.digest('hex');
    return{
        salt:salt,
        passwordHash:value
    }
};


function saltHashPassword(userPassword){
    var salt= getRandomString(16);
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}


function checkHashPassword(userPassword,salt){
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}



app.post('/register/',(req,res,next)=>{
   

    
    var plaint_password=req.body.password;
    var hash_data=saltHashPassword(plaint_password);
    var salt=hash_data.salt;
    var password=hash_data.passwordHash;
    var name=req.body.name;
    var email=req.body.email;
    

    conn.query('select * from user where email=?',[email],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });

        if (result=result.length)
        res.json('user already exists');
        else
        {
        conn.query('INSERT INTO `user`(`name`, `email`, `password`, `rating`, `salt`) VALUES (?,?,?,?,?)',[name,email,password,0,salt],function(err,result,fields){
            conn.on('error',function(err){
                console.log('MYSQL Error',err);
                res.json('regist error',err)
            });
            res.json('Success!');
        })
        }
        });
    
})
/*================================================ Login ==============================================================*/


app.post('/login/',(req,res,next)=>{
   
    var user_password=req.body.password;
    var email=req.body.email;
    
    

    conn.query('select * from user where email=?',[email],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });

        if (result && result.length){
            var salt = result[0].salt;
            var dbPassword = result[0].password;
            var givenPassword = checkHashPassword(user_password,salt).passwordHash;
            if(dbPassword == givenPassword){
                res.end(JSON.stringify(result[0]));
            }else{
                res.end(JSON.stringify('Wrong password'));
            }
        }else
            {
                    res.json('User Does not exist')
             }
        });
    
})

app.get('/login/', function (req, res) {
    res.send('Login a funcionar')
  })



/*===========================================================/GROUPS/==========================================================*/

app.get('/group',(req,res)=>{
    conn.query('SELECT ideaGroups.*,user.email FROM ideaGroups INNER JOIN user ON ideaGroups.`userID_creator`=user.userID ',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

//GET USER INFO
app.get('/user/:email',(req,res)=>{
    var userEmail=req.params.email;
    conn.query('SELECT userID,name,email,rating,user_bio FROM `user` WHERE email = ?',[userEmail],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

app.post('/group/new',(req,res,next)=>{
   
    
    var ideaTitle=req.body.name;
    var description =req.body.description;
    
    conn.query('INSERT INTO `ideaGroups`(`name`, `description`, `type`,`userID_creator`) VALUES (?,?,?,?)',[ideaTitle,description,"idea",7],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    res.send("oke")

        
    
})



//--------------------------------------------------------------- FEEDBACK

app.get('/feedback/userId/:userID',(req,res)=>{
    var userID=req.params.userID;
    conn.query('SELECT feedback.*,user.email FROM feedback INNER JOIN user ON feedback.userID_fk=user.userID WHERE userID = ?',[userID],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});






app.get('/feedback/:groupId',(req,res)=>{
    var groupId=req.params.groupId;
    conn.query('SELECT feedback.*,user.email FROM feedback INNER JOIN user ON feedback.userID_fk=user.userID WHERE grupoID_fk = ? ORDER BY rating DESC ',[groupId],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

app.post('/feedback/new',(req,res,next)=>{
   
    var text=req.body.text;
   
    
    conn.query('INSERT INTO `feedback`(`text`, `rating`, `userID_fk`,`grupoID_fk`) VALUES (?,?,?,?)',[text,1,7,1],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

        
    
})

//--------------------------------------------------- CHALLENGES ------------------------

app.get('/challenge',(req,res)=>{
    conn.query('SELECT challenges.*,user.email FROM challenges INNER JOIN user ON challenges.challenge_userID=user.userID',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});




app.get('/feedback/challenge/:challengeId',(req,res)=>{
    var cID=req.params.challengeId;
    conn.query('SELECT challengesFeedback.*,user.email FROM challengesFeedback INNER JOIN user ON challengesFeedback.cUserID_fk=user.userID WHERE cChallengeID_fk = ? ORDER BY cFeedbackRating DESC',[cID],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

var curday = function(){
    today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; 
    var yyyy = today.getFullYear();
    
    if(dd<10) dd='0'+dd;
    if(mm<10) mm='0'+mm;
    return (yyyy+"-"+mm+"-"+dd);
    
}


app.post('/feedback/challenge/new',(req,res,next)=>{
   
    var challengeText=req.body.cFeedbackText;
   
    

    conn.query('INSERT INTO `challengesFeedback`(`cFeedbackText`, `cFeedbackRating`, `cUserID_fk`, `cChallengeID_fk`) VALUES (?,?,?,?)',[challengeText,1,7,1],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    res.send("oke")

        
    
})


app.post('/challenge/new',(req,res,next)=>{
   
    
    var challengeTitle = req.body.title;
    var challengeDescription = req.body.description;
    var challengeInstructions = req.body.instructions;
    var endDate = req.body.end_date;
    console.log(endDate);

   
        conn.query('INSERT INTO `challenges`(`title`, `description`, `instructions`,`creation_date`,`end_date`,`challenge_userID`) VALUES (?,?,?,CURDATE(),?,?)',[challengeTitle,challengeDescription,challengeInstructions,endDate,7],function(err,result,fields){
            conn.on('error',function(err){
                console.log('MYSQL Error',err);
            });
            res.send("oke fine")
        }); 
    

    

    
    
        
    
})





///----------------------------------------------------------------------------------------





//CREATE NEW IDEA

/*app.post('/idea/new'),(req,res,next)=>{
    var ideaTitle = req.body.ideaTitle; 
    var ideaDescription=req.body.ideaDescription;


    conn.query('INSERT INTO idea ("ideaTitle","texto","rating","userID_fk","groupID_fk") VALUES (?,?,?,?,?)',[ideaTitle,ideaDescription,0,0,0],function(err,result,fields){
        conn.on('error',function(err){
            console.log("MYSQL Error",err);
            res.json('Error creating new idea',err)
        });
        res.json('New idea Created sucessfully');
    })
}

*/

//CREATE NEW GROUP






//'INSERT INTO `groups`(`name`, `description`, `type`) VALUES (?,?,?)



//»»»»»»»»»»»»»»»»»»»»»»»Start Server

app.listen(3000,()=>{
    console.log("Olá ,está tudo a funcionar como o Indiano ensinou");
})


app.get('/', (req, res,next) => {
    /* console.log('Password: 123456');
     console.log('Encrypt:'+saltHashPassword("123456").passwordHash);
     console.log('Salt:'+saltHashPassword("123456").salt);*/
     res.send('Servidor a bombar!')
    
     
 });
 console.log("Hoje é dia: "+curday())