var crypto = require('crypto');
var express = require('express');
var uuid = require('uuid');
var mysql = require('mysql');
var bodyParser = require('body-parser');
require('events').EventEmitter.prototype.setMaxListeners=100;


var app = express()
app.use(bodyParser.json()); //JSON params
app.use(bodyParser.urlencoded({extended:true})); // accpet url encoded params


//=================================================== MySQL CONNECTION===========================================//
var conn = mysql.createConnection({
    host:'remotemysql.com',
    user:'M1dLL4sKdM',
    password:'sEWth7V12F',
    database:'M1dLL4sKdM'
});

//===================================================== REGISTER =================================================//


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
            res.json("Sucess!");
        })
        }
        });
    
})
//===================================================== LOGIN =================================================//


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



//===================================================== GROUPS=================================================//

app.get('/group/:groupID',(req,res)=>{
    var id = req.params.groupID;
    conn.query('SELECT ideaGroups.*,user.email FROM ideaGroups INNER JOIN user ON ideaGroups.`userID_creator`=user.userID WHERE groupID = ?',[id],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});



app.post('/group/new',(req,res,next)=>{
   
    
    var ideaTitle=req.body.name;
    var description =req.body.description;
    var id =req.body.userID_creator;
    
    conn.query('INSERT INTO `ideaGroups`(`name`, `description`,`userID_creator`) VALUES (?,?,?)',[ideaTitle,description,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
        res.send(result);
    }); 

    

        
    
})



//===================================================== FEEDBACK =================================================//

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
    var userFk = req.body.userID_fk;
    var grupoIDFk = req.body.grupoID_fk
   
    
    conn.query('INSERT INTO `feedback`(`text`,`rating`,`userID_fk`,`grupoID_fk`) VALUES (?,?,?,?)',[text,1,userFk,grupoIDFk],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
            
        });

        res.send(result)
    }); 

        
    
})



//===================================================== CHALLENGES =================================================//

app.get('/challenge',(req,res)=>{
    conn.query('SELECT challenges.*,user.email FROM challenges INNER JOIN user ON challenges.challenge_userID=user.userID',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

app.get('/userChallenges/:userID',(req,res)=>{
    var uid = req.params.userID;
    conn.query('SELECT userChallenges.*,challenges.*,user.email FROM userChallenges INNER JOIN challenges ON userChallenges.challengeID_fk = challenges.challengeID INNER JOIN user ON userChallenges.userID_fk = user.userID WHERE userID_fk = ?',[uid],(err,rows,fields)=>{
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
    var cUserFK = req.body.cUserID_fk;
    var challengeIDFk = req.body.cChallengeID_fk
   
    

    conn.query('INSERT INTO `challengesFeedback`(`cFeedbackText`, `cFeedbackRating`, `cUserID_fk`, `cChallengeID_fk`) VALUES (?,?,?,?)',[challengeText,1,cUserFK,challengeIDFk],function(err,result,fields){
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
    var uid = req.body.userID;
    console.log(endDate);

   
        conn.query('INSERT INTO `challenges`(`title`, `description`, `instructions`,`creation_date`,`end_date`,`challenge_userID`) VALUES (?,?,?,CURDATE(),?,?)',[challengeTitle,challengeDescription,challengeInstructions,endDate,uid],function(err,result,fields){
            conn.on('error',function(err){
                console.log('MYSQL Error',err);
            });
            res.send(result)
        }); 
    

    

    
    
        
    
})
//===================================================== RATING =================================================//

app.get('/feedback/challenge/rating/:challengeId',(req,res)=>{
    var cID=req.params.challengeId;
    conn.query('SELECT COUNT(likeID) AS NumberOfLikes FROM challengeFeebackLikes where cFeedbackID_fk = ? ',[cID],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});



app.post('/feedback/challenges/increaseRating',(req,res,next)=>{
   
    
    var id = req.body.userID;
    var cFbID = req.body.cFeedbackID;
   
    

    conn.query('INSERT INTO challengeFeebackLikes( cFeedbackID_fk, userID_fk) VALUES (?,?)',[cFbID,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    

        
    
})



app.post('/feedback/challenges/decreaseRating',(req,res,next)=>{
   
    
    var id = req.body.userID;
    var cFbID = req.body.cFeedbackID;
   
    

    conn.query('DELETE FROM `challengeFeebackLikes` WHERE cFeedbackID_fk = ? AND userID_fk =?',[cFbID,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    

        
    
})

app.get('/challenge/feedback/likes',(req,res)=>{
    
    conn.query('SELECT * FROM `challengeFeebackLikes`',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

//---------


app.get('/feedback/group/rating/:groupId',(req,res)=>{
    var ID=req.params.groupId;
    conn.query('SELECT COUNT(likeID) AS NumberOfLikes FROM groupFeedbackLikes where feedbackID_fk = ? ',[ID],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});


app.get('/groups/feedback/likes',(req,res)=>{
    
    conn.query('SELECT * FROM `groupFeedbackLikes`',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});



app.post('/feedback/groups/increaseRating',(req,res,next)=>{
   
    
    var id = req.body.userID;
    var fbID = req.body.feedbackID;
   
    

    conn.query('INSERT INTO groupFeedbackLikes( feedbackID_fk, userID_fk) VALUES (?,?)',[fbID,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    

        
    
})


app.post('/feedback/groups/decreaseRating',(req,res,next)=>{
   

    var id = req.body.userID;
    var fbID = req.body.feedbackID;
   

    conn.query('DELETE FROM `groupFeedbackLikes` WHERE feedbackID_fk = ? AND userID_fk =?',[fbID,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})





/*app.post('/feedback/group/newRating',(req,res,next)=>{
   
    
    var feedbackID = req.body.feedbackID;
    var rating = req.body.rating;
   
    

    conn.query('UPDATE feedback SET rating = ? WHERE feedbackID = ?',[rating,feedbackID],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    

        
    
})*/

//===================================================== USER =================================================//
app.get('/user/:email',(req,res)=>{
    var userEmail=req.params.email;
    conn.query('SELECT userID,name,email,rating,user_bio,funds FROM `user` WHERE email = ?',[userEmail],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});


app.get('/users/',(req,res)=>{
    
    conn.query('SELECT * FROM user ORDER BY rating DESC',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});


app.post('/user/increaseRating',(req,res,next)=>{
   

    var id = req.body.userID;
    
   

    conn.query('UPDATE user SET rating = rating +1 WHERE userID=? ',[id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})

app.post('/user/increaseFunds',(req,res,next)=>{
   

    var id = req.body.userID;
    
   

    conn.query('UPDATE user SET funds = funds +1 WHERE userID=? ',[id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})

app.post('/user/decreaseCoins',(req,res,next)=>{
   
    var value = req.body.value;
    var id = req.body.userID;
    
    
   

    conn.query('UPDATE user SET funds = funds -? WHERE userID=?',[value,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})

app.post('/user/decreaseFunds',(req,res,next)=>{
   

    var id = req.body.userID;
    
   

    conn.query('UPDATE user SET funds = funds -1 WHERE userID=? ',[id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})




app.post('/user/updateBio',(req,res,next)=>{
   

    var id = req.body.userID;
    var bio =req.body.bio;
    
   

    conn.query('UPDATE user SET user_bio = ? WHERE userID = ? ',[bio,id],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

})



//===================================================== REWARDS =================================================//
app.get('/reward/',(req,res)=>{
    
    conn.query('SELECT * FROM rewards ORDER BY price ASC',(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});

//===================================================== asdasd =================================================//

app.get('/userGroups/:userID',(req,res)=>{

     var uid =req.params.userID;
    
    conn.query('SELECT userGroups.*,ideaGroups.*,user.email FROM userGroups INNER JOIN ideaGroups ON userGroups.groupID_fk = ideaGroups.groupID INNER JOIN user ON userGroups.userID_fk = user.userID WHERE userID_fk = ? ',[uid],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});


app.post('/userGroups/newUserGroup',(req,res,next)=>{
   
    
    var uid = req.body.userID;
    var gid = req.body.groupID;
   
    

    conn.query('INSERT INTO `userGroups`( `userID_fk`, `groupID_fk`) VALUES (?,?)',[uid,gid],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
        res.send(result);
    }); 

    

        
    
})


app.post('/userChallenge/newUserChallenge',(req,res,next)=>{
   
    
    var uid = req.body.userID;
    var cid = req.body.challengeID;
   
    

    conn.query('INSERT INTO `userChallenges`( `userID_fk`, `challengeID_fk`) VALUES (?,?)',[uid,cid],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
    }); 

    

        
    
})



//===================================================== TRANSACTIONS =================================================//

app.get('/transactions/:userID',(req,res)=>{
   
    var id =req.params.userID;
    
    conn.query('SELECT transactions.*,rewards.* FROM transactions INNER JOIN rewards ON transactions.rewardsID_fk = rewards.rewardID WHERE transactions.userID_fk = ?',[id],(err,rows,fields)=>{
        if(!err)
        res.send(rows);
        else
        console.log(err);
    })
});


app.post('/newTransaction',(req,res,next)=>{
   

    var uid = req.body.userID;
    var rid =req.body.rewardID;
    
   

    conn.query('INSERT INTO `transactions`( `userID_fk`, `rewardsID_fk`) VALUES (?,?)',[uid,rid],function(err,result,fields){
        conn.on('error',function(err){
            console.log('MYSQL Error',err);
        });
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
    console.log("Server Started");
})


app.get('/', (req, res,next) => {
    /* console.log('Password: 123456');
     console.log('Encrypt:'+saltHashPassword("123456").passwordHash);
     console.log('Salt:'+saltHashPassword("123456").salt);*/
     res.send('Servidor a bombar!')
    
     
 });
 console.log("Hoje é dia: "+curday())