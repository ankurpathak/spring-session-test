//userBusiness
db.user.aggregate([{
    "$lookup": {
        from: "businesses",
        localField: "businessIds",
        foreignField: "_id",
        as: "businesses"
    }
}]);

db.runCommand({
    create: "userBusiness", viewOn: "user", pipeline: [{
        "$lookup": {
            from: "business",
            localField: "businessIds",
            foreignField: "_id",
            as: "businesses"
        }
    }]
});

db.customer.aggregate([
    {
        "$lookup": {
            from: "user",
            localField: "_id.userId",
            foreignField: "_id",
            as: "user"
        }

    }

]);

db.runCommand({
    create: "customerUser", viewOn: "customer", pipeline: [
        {
            "$lookup": {
                from: "users",
                localField: "_id.userId",
                foreignField: "_id",
                as: "user"
            }
        }]
})