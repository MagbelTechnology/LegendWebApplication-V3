SELECT
     am_ad_branch."BRANCH_ID" AS am_ad_branch_BRANCH_ID,
     am_ad_category."category_ID" AS am_ad_category_category_ID,
     am_Asset_A."Asset_id" AS am_Asset_A_Asset_id,
     am_Asset_A."Description" AS am_Asset_A_Description,
     am_ad_branch."BRANCH_NAME" AS am_ad_branch_BRANCH_NAME,
     am_ad_category."category_name" AS am_ad_category_category_name,
     am_Asset_A."Cost_Price" AS am_Asset_A_Cost_Price,
     am_Asset_A."monthly_dep" AS am_Asset_A_monthly_dep,
     am_Asset_A."Accum_dep" AS am_Asset_A_Accum_dep,
     am_Asset_A."NBV" AS am_Asset_A_NBV,
     am_Asset_A."Date_purchased" AS am_Asset_A_Date_purchased
FROM
     "dbo"."am_Asset" am_Asset_A INNER JOIN "dbo"."am_ad_branch" am_ad_branch ON am_Asset_A."Branch_ID" = am_ad_branch."BRANCH_ID"
     INNER JOIN "dbo"."am_ad_category" am_ad_category ON am_Asset_A."Category_ID" = am_ad_category."category_ID"