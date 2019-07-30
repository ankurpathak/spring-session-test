//userBusiness
db.users.aggregate([{
    "$lookup": {
        from: "businesses",
        localField: "businessIds",
        foreignField: "_id",
        as: "businesses"
    }
}]);

db.runCommand({
    create: "userBusiness", viewOn: "users", pipeline: [{
        "$lookup": {
            from: "businesses",
            localField: "businessIds",
            foreignField: "_id",
            as: "businesses"
        }
    }]
});

db.userBusiness.find();