package com.ecomapp.admin.Repositories

import android.net.Uri
import com.ecomapp.admin.Api.NotificationApi
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Models.FirebaseNotificationModel
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Models.ProductImageModel
import com.ecomapp.admin.Models.PushNotification
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.febric.Models.OrderIdModel
import com.ecomapp.febric.Models.OrderModel
import com.ecomapp.febric.Models.ProductIdModel
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Models.SizeModel
import com.ecomapp.febric.Repositories.Response
import com.ecomapp.wear.Models.NotificationModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DataRepository @Inject constructor(
    val database: FirebaseFirestore,
    val storage: FirebaseStorage,
    val notificationApi: NotificationApi
) {

    var bannerList = ArrayList<BannerModel>()
    var mainCatList = ArrayList<MainCatModel>()
    var subCatList = ArrayList<SubCatModel>()
    var AllProductList = ArrayList<ProuctModel>()
    var productList = ArrayList<ProuctModel>()
    var productDetail = ProuctModel()
    var ordersList = ArrayList<OrderModel>()
    var productIdList = ArrayList<ProductIdModel>()

    suspend fun LoadHomeBanners(): Response<ArrayList<BannerModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection("HomeBanners").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            bannerList.addAll(snapshot.toObjects(BannerModel::class.java))
        }

        return try {
            Response.Sucess(bannerList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun UpdateBanners(pos: String, bannerModel: BannerModel): Response<String> {

        val upload = withContext(Dispatchers.IO) {
            val ref = storage.reference.child("HomeBanners")
                .child("cover_" + pos)
            ref.putFile(Uri.parse(bannerModel.mainImage)).await()
            ref.downloadUrl.await()
        }

        val snapshot = withContext(Dispatchers.IO) {
            bannerModel.mainImage = upload.toString()
            database.collection("HomeBanners")
                .document(pos)
                .set(bannerModel).await()
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadBannersProducts(loadUsing: String): Response<ArrayList<ProuctModel>> {
        val snapshot = withContext(Dispatchers.IO) {
            database.collection("HomeBanners")
                .document(loadUsing)
                .collection("Products")
                .get().await()
        }


        val productIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val productId = document.getString("productId")
                productIds.add(productId!!)
            }
        }

        val snapshot2 = withContext(Dispatchers.IO) {
            database.collection("AllProducts")
                .orderBy("productId", Query.Direction.DESCENDING)
                .get().await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (favID in productIds) {
                    if (favID.equals(productId)) {
                        productList.add(document.toObject(ProuctModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(productList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadProducts(
        parentCatName: String,
        mainCatName: String,
        subCatName: String
    ): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCatName)
                .collection("Products").get().await()
        }


        val productIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val productId = document.getString("productId")
                productIds.add(productId!!)
            }
        }


        val snapshot2 = withContext(Dispatchers.IO) {
            database.collection("AllProducts").get().await()
        }
        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (id in productIds) {
                    if (id.equals(productId)) {
                        productList.add(document.toObject(ProuctModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(productList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadMainCategories(catName: String): Response<ArrayList<MainCatModel>> {


        val snapshot = withContext(Dispatchers.IO) {
            database.collection("Categories")
                .document(catName)
                .collection("MainCategories")
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            mainCatList.addAll(snapshot.toObjects(MainCatModel::class.java))
        }

        return try {
            Response.Sucess(mainCatList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun AddMainCategory(parentCat: String, mainCatModel: MainCatModel): Response<String> {

        val upload = withContext(Dispatchers.IO) {
            val ref = storage.reference.child("Categories")
                .child(parentCat).child(mainCatModel.mainCatName!!)
            ref.putFile(Uri.parse(mainCatModel.mainCatImage)).await()
            ref.downloadUrl.await()
        }

        val insert = withContext(Dispatchers.IO) {
            mainCatModel.mainCatImage = upload.toString()
            database.collection("Categories")
                .document(parentCat)
                .collection("MainCategories")
                .document(mainCatModel.mainCatName!!).set(mainCatModel).await()
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun deleteMainCategory(parentCat: String, mainCat: String): Response<String> {

        val insert = withContext(Dispatchers.IO) {
            database.collection("Categories")
                .document(parentCat)
                .collection("MainCategories")
                .document(mainCat).delete().await()
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadSubCatigories(
        parentCatName: String,
        mainCatName: String
    ): Response<ArrayList<SubCatModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            subCatList.addAll(snapshot.toObjects(SubCatModel::class.java))
        }

        return try {
            Response.Sucess(subCatList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun AddSubCategory(
        parentCatName: String,
        mainCatName: String,
        subCatModel: SubCatModel
    ): Response<String> {

        val insert = withContext(Dispatchers.IO) {

            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCatModel.subCatName!!)
                .set(subCatModel).await()
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun deleteSubCategory(
        parentCatName: String,
        mainCatName: String,
        subCateName: String
    ): Response<String> {

        val insert = withContext(Dispatchers.IO) {

            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCateName)
                .delete().await()
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadAllProducts(): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection("AllProducts").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            AllProductList.addAll(snapshot.toObjects(ProuctModel::class.java))
        }

        return try {
            Response.Sucess(AllProductList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun AddNewProduct(
        parentCatName: String, mainCatName: String, subCatName: String,
        prouctModel: ProuctModel,
        sizeList: ArrayList<SizeModel>,
        imageList: ArrayList<ProductImageModel>,
        selectedList: ArrayList<String>
    ): Response<String> {

        val uploadImageList: ArrayList<ProductImageModel> = ArrayList()

        val upload = withContext(Dispatchers.IO) {

            for (singleImage in imageList) {
                val ref = storage.reference.child("ProductsImages")
                    .child(System.currentTimeMillis().toString())
                ref.putFile(Uri.parse(singleImage.ImageUrl)).await()

                val getUrl = withContext(Dispatchers.IO) {
                    ref.downloadUrl.await()
                }
                uploadImageList.add(ProductImageModel(getUrl.toString()))
            }
            prouctModel.productMainImage = uploadImageList.get(0).ImageUrl
        }

        val addIntoAllProduct = withContext(Dispatchers.IO) {
            database.collection("AllProducts")
                .document(prouctModel.productId!!)
                .set(prouctModel)
                .await()
        }

        val addProductImages = withContext(Dispatchers.IO) {

            var i: Int = 1

            for (singleUplodUrl in uploadImageList) {

                database.collection("AllProducts")
                    .document(prouctModel.productId!!)
                    .collection("ProductImages")
                    .document(i.toString())
                    .set(singleUplodUrl)
                    .await()
                i++
            }
        }

        val addSizes = withContext(Dispatchers.IO) {

            var i: Int = 1

            for (singleSize in sizeList) {

                database.collection("AllProducts")
                    .document(prouctModel.productId!!)
                    .collection("Sizes")
                    .document(i.toString())
                    .set(singleSize)
                    .await()
                i++
            }
        }

        val insertIntoCat = withContext(Dispatchers.IO) {

            val productIdModel: ProductIdModel = ProductIdModel(prouctModel.productId)

            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCatName)
                .collection("Products")
                .document(prouctModel.productId!!)
                .set(productIdModel).await()
        }

        val insertIntoBanners = withContext(Dispatchers.IO) {

            val productIdModel: ProductIdModel = ProductIdModel(prouctModel.productId)

            for (single in selectedList) {
                database.collection("HomeBanners")
                    .document(single)
                    .collection("Products")
                    .document(productIdModel.productId!!)
                    .set(productIdModel).await()
            }
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadSingleProduct(productId: String): Response<ProuctModel> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection("AllProducts")
                .document(productId)
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            productDetail = snapshot.toObject(ProuctModel::class.java)!!
        }

        return try {
            Response.Sucess(productDetail)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun updateProduct(prouctModel: ProuctModel): Response<String> {

        val addIntoAllProduct = withContext(Dispatchers.IO) {
            database.collection("AllProducts")
                .document(prouctModel.productId!!)
                .set(prouctModel)
                .await()
        }
        return try {
            Response.Sucess("success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun deleteProductFromBanner(pos: String, productId: String): Response<String> {

        val product = withContext(Dispatchers.IO) {
            database.collection("HomeBanners")
                .document(pos)
                .collection("Products")
                .document(productId)
                .delete().await()
        }
        return try {
            Response.Sucess("success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun deleteProductFromCat(
        parentCatName: String,
        mainCatName: String,
        subCatName: String,
        productId: String
    ): Response<String> {

        val delete = withContext(Dispatchers.IO) {

            database.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCatName)
                .collection("Products")
                .document(productId)
                .delete().await()
        }
        return try {
            Response.Sucess("success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun deleteProduct(productId: String): Response<String> {
        val addIntoAllProduct = withContext(Dispatchers.IO) {
            database.collection("AllProducts")
                .document(productId)
                .delete()
                .await()
        }
        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun loadOrders(orderCat: String): Response<ArrayList<OrderModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            database.collection(orderCat)
                .get().await()
        }

        val orderIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val orderId = document.getString("orderId")
                orderIds.add(orderId!!)
            }
        }
        val snapshot2 = withContext(Dispatchers.IO) {
            database.collection("Orders").get().await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val orderId = document.getString("orderId")
                for (id in orderIds) {
                    if (id.equals(orderId)) {
                        ordersList.add(document.toObject(OrderModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(ordersList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun DeliveredOrder(
        orderId: String,
        userId: String,
        productId: String,
        productMainImage: String
    ): Response<String> {

        val updateDeliveryDate = withContext(Dispatchers.IO) {

            val simpleDate = SimpleDateFormat("dd/MM/yyyy")
            val dateInStr = simpleDate.format(Date())

            database.collection("Orders")
                .document(orderId)
                .update("deliveryDate", "Deliverd at " + dateInStr)
                .await()
        }

        val deleteFromPending = withContext(Dispatchers.IO) {

            database.collection("PendingOrders").document(orderId)
                .delete()
                .await()
        }

        val addToDelivered = withContext(Dispatchers.IO) {

            val order = OrderIdModel(orderId)

            database.collection("DeliveredOrders").document(orderId)
                .set(order)
                .await()
        }

        val userDeleteFromPending = withContext(Dispatchers.IO) {

            database.collection("users")
                .document(userId)
                .collection("pendingOrders")
                .document(orderId)
                .delete()
                .await()
        }

        val userAddToDelivered = withContext(Dispatchers.IO) {

            val order = OrderIdModel(orderId)

            database.collection("users")
                .document(userId)
                .collection("deliveredOrders")
                .document(orderId)
                .set(order)
                .await()
        }

        withContext(Dispatchers.IO) {

            val notificationModel = NotificationModel(
                "Your order is delivered successfully for tracking no : " + orderId,
                "Delivered",
                productId,
                productMainImage
            )

            database.collection("users")
                .document(userId)
                .collection("notifications")
                .document(System.currentTimeMillis().toString())
                .set(notificationModel)
                .await()
        }

        val notify = withContext(Dispatchers.IO) {

            val notification = PushNotification(
                FirebaseNotificationModel(
                    "Delivered successfully",
                    "Your order is delivered successfully for tracking no : " + orderId
                ), "/topics/"+userId
            )

            try {
                val respose = notificationApi.postNotification(notification)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun CancelOrder(
        orderId: String,
        userId: String,
        productId: String,
        productMainImage: String
    ): Response<String> {
        val updateDeliveryDate = withContext(Dispatchers.IO) {

            val simpleDate = SimpleDateFormat("dd/MM/yyyy")
            val dateInStr = simpleDate.format(Date())

            database.collection("Orders")
                .document(orderId)
                .update("deliveryDate", "Cancelled at " + dateInStr)
                .await()
        }
        val deleteFromPending = withContext(Dispatchers.IO) {

            database.collection("PendingOrders").document(orderId)
                .delete()
                .await()
        }

        val addToDelivered = withContext(Dispatchers.IO) {

            val order = OrderIdModel(orderId)

            database.collection("CancelledOrders").document(orderId)
                .set(order)
                .await()
        }

        val userDeleteFromPending = withContext(Dispatchers.IO) {

            database.collection("users")
                .document(userId)
                .collection("pendingOrders")
                .document(orderId)
                .delete()
                .await()

        }

        val userAddToDelivered = withContext(Dispatchers.IO) {

            val order = OrderIdModel(orderId)

            database.collection("users")
                .document(userId)
                .collection("cancelledOrders")
                .document(orderId)
                .set(order)
                .await()
        }

            withContext(Dispatchers.IO) {

            val notificationModel = NotificationModel(
                "Your order is cancelled by seller for tracking no : " + orderId,
                "Cancelled",
                productId,
                productMainImage
            )

            database.collection("users")
                .document(userId)
                .collection("notifications")
                .document(System.currentTimeMillis().toString())
                .set(notificationModel)
                .await()
        }

        val notify = withContext(Dispatchers.IO) {

            val notification = PushNotification(
                FirebaseNotificationModel(
                    "Order Cancelled",
                    "Your order is cancelled by seller for tracking no : " + orderId
                ), "/topics/"+userId
            )

            try {
                notificationApi.postNotification(notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun sendFBNotification(title : String, text : String) : Response<String>{

        val notify = withContext(Dispatchers.IO) {

            val notification = PushNotification(
                FirebaseNotificationModel(
                    title,
                    text
                ), "/topics/new"
            )

            try {
                notificationApi.postNotification(notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        return try {
            Response.Sucess("Success")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

}